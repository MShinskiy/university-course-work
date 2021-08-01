#include "ros/ros.h"
#include "geometry_msgs/Twist.h"
#include "nav_msgs/Odometry.h"
#include <fstream>
#include <time.h>
#include <iomanip>
#include "sensor_msgs/LaserScan.h"

using namespace std;

ofstream odomVelFile;

struct EulerAngles{
	double roll, pitch, yaw;
};

struct Quaternion{
	double w, x, y, z;
};

EulerAngles ToEulerAngles(Quaternion q){
	EulerAngles angles;
	//roll (x-asis rotation)
	double sinr_cosp = +2.0 * (q.w * q.x + q.y * q.z);
	double cosr_cosp = +1.0 - 2.0 * (q.x * q.x + q.y * q.y);
	angles.roll = atan2(sinr_cosp, cosr_cosp);
	//pitch (y-axis rotation)
	double sinp = +2.0 * (q.w * q.y - q.z * q.x);
	if(fabs(sinp) >= 1)
		angles.pitch = copysign(M_PI/2, sinp); //use 90 degrees if out of range
	else 
		angles.pitch = asin(sinp);
	//yaw (z-axis rotation)
	double siny_cosp = +2.0 * (q.w * q.z + q.x * q.y);
	double cosy_cosp = +1.0 - 2.0 * (q.y * q.y + q.z * q.z);
	angles.yaw = atan2(siny_cosp, cosy_cosp);
	return angles;
}

class Stopper {
public:
	// Tunable parameters
	constexpr const static double FORWARD_SPEED_LOW = 0.1;
	constexpr const static double FORWARD_SPEED_HIGH = 0.2;
	constexpr const static double FORWARD_SPEED_SHIGH = 0.8;
	constexpr const static double FORWARD_SPEED_STOP = 0;
	constexpr const static double TURN_LEFT_SPEED_HIGH = 1.0;
	constexpr const static double TURN_LEFT_SPEED_LOW = 0.3;
	constexpr const static double TURN_RIGHT_SPEED_HIGH = -2.4;
	constexpr const static double TURN_RIGHT_SPEED_LOW = -0.3;
	constexpr const static double TURN_RIGHT_SPEED_MIDDLE = -0.6;
	Stopper();
	void startMoving();
	void moveForward(double forwardSpeed);
	void moveStop();
	void moveRight(double turn_right_speed = TURN_RIGHT_SPEED_HIGH);
	void moveForwardRight(double forwardSpeed, double turn_right_speed);
	void odomCallback(const nav_msgs::Odometry::ConstPtr& odomMsg);
	void scanCallback(const sensor_msgs::LaserScan::ConstPtr& scan);
	int stage;
	double PositionX, PositionY;
	double robVelocity;
	double startTime; //start time
	double frontRange, mleftRange, leftRange, rightRange, mrightRange, backRange, leftbackRange, rightbackRange;

	Quaternion robotQuat;
	EulerAngles robotAngles;
	double robotHeadAngle;
	void transformMapPoint(ofstream & fp, double laserRange, double laserTh, double robotTh, double robotX, double robotY);

private:
	ros::NodeHandle node;
	ros::Publisher commandPub; // Publisher to the robot's velocity command topic
	ros::Subscriber odomSub;   //Subscriber to robot's odometry topic
	ros::Subscriber laserSub;  //Subscriber to robot's laser topic
};

Stopper::Stopper(){
//Advertise a new publisher for the simulated robot's velocity command topic at 10Hz
	commandPub = node.advertise<geometry_msgs::Twist>("cmd_vel", 10);
	// subscribe to the odom topic
 	odomSub = node.subscribe("odom", 20, &Stopper::odomCallback, this);
	laserSub = node.subscribe("scan", 1, &Stopper::scanCallback, this);
}

//send a velocity command
void Stopper::moveForward(double forwardSpeed){
	geometry_msgs::Twist msg;//The default constructor to set all commands to 0
	msg.linear.x = forwardSpeed; //Drive forward at a given speed along the x-axis.
	commandPub.publish(msg);
}

void Stopper::moveStop(){
	geometry_msgs::Twist msg;
	msg.linear.x = FORWARD_SPEED_STOP;
	commandPub.publish(msg);
}

void Stopper::moveRight(double turn_right_speed){
	geometry_msgs::Twist msg;
	msg.angular.z = turn_right_speed;
	commandPub.publish(msg);
}

void Stopper::moveForwardRight(double forwardSpeed, double turn_right_speed){
	//move forward and right at the same time
	geometry_msgs::Twist msg;
	msg.linear.x = forwardSpeed;
	msg.angular.z = turn_right_speed;
	commandPub.publish(msg);
}

//add callback funciton to determine the robot position.
void Stopper::odomCallback(const nav_msgs::Odometry::ConstPtr& odomMsg){
	PositionX = odomMsg->pose.pose.position.x;
	PositionY = odomMsg->pose.pose.position.y;
	robVelocity = odomMsg->twist.twist.linear.x;

	robotQuat.x = odomMsg->pose.pose.orientation.x;
	robotQuat.y = odomMsg->pose.pose.orientation.y;
	robotQuat.z = odomMsg->pose.pose.orientation.z;
	robotQuat.w = odomMsg->pose.pose.orientation.w;
	robotAngles = ToEulerAngles(robotQuat);
	robotHeadAngle = robotAngles.yaw;

	double currentSeconds = ros::Time::now().toSec();
	double elapsedTime = currentSeconds - startTime;
	odomVelFile << elapsedTime << " " << robVelocity << endl;
}

void Stopper::scanCallback(const sensor_msgs::LaserScan::ConstPtr& scan){
	frontRange = scan->ranges[0];	//get at 0 
	mleftRange = scan->ranges[89];	//get at -pi/4 
	leftRange = scan->ranges[179];	//get at -pi/2 
	leftbackRange = scan->ranges[269];	//get at 3pi/4
	backRange = scan->ranges[359];	//get at pi
	rightbackRange = scan->ranges[449];	//get at -3pi/4
	rightRange = scan->ranges[539];	//get at pi/2
	mrightRange = scan->ranges[629];//get at pi/4
}

void Stopper::transformMapPoint(ofstream & fp, double laserRange, double laserTh, double robotTh, double robotX, double robotY){
	double transX, transY, homeX = 0.3, homeY = 0.3;
	transX = laserRange * cos(robotTh + laserTh) + robotX;
	transY = laserRange * sin(robotTh + laserTh) + robotY;
	
	if(transX < 0) transX = homeX;
	else transX += homeX;

	if(transY < 0) transY = homeX; 
	else transY += homeY;

	fp << transX << " " << transY << endl;
}

void Stopper::startMoving(){
	ofstream odomTrajFile;
	odomTrajFile.open("/home/local/CAMPUS/ms18975/M-Drive/ros_workspace/src/tutorial_pkg/odomTrajData.csv", ios::trunc);
	odomVelFile.open("/home/local/CAMPUS/ms18975/M-Drive/ros_workspace/src/tutorial_pkg/odomVelData.csv", ios::trunc);

	startTime = ros::Time::now().toSec(); //obtain the start time

	ofstream laserFile;
	laserFile.open("/home/local/CAMPUS/ms18975/M-Drive/ros_workspace/src/tutorial_pkg/laserData.csv", ios::trunc);
	int i = 0;	//the index to record laser scan data

	double frontAngle = 0, mleftAngle = 0.785, leftAngle = 1.57;
	double rightAngle = -1.57, mrightAngle = -0.785;
	ofstream laserMapFile;
	laserMapFile.open("/home/local/CAMPUS/ms18975/M-Drive/ros_workspace/src/tutorial_pkg/laserMapData.csv", ios::trunc);

	stage = 1;
	ros::Rate rate(20);  //Define rate for repeatable operations.
	ROS_INFO("Start moving");
	while(ros::ok()){
		//There is some external bug with a few first steps
		if(i > 6) {
			switch(stage){
				//Start off from moving fast in a straight line
				case 1:
					if(frontRange > 1.2)
						moveForward(FORWARD_SPEED_SHIGH);
					else 
						stage = 2;
					break;
				//Turn towards right obstacle in first gap
				case 2:
					if(mrightRange > 0.5)
						moveForwardRight(FORWARD_SPEED_HIGH, TURN_RIGHT_SPEED_MIDDLE);
					else
						stage = 3;
					break;
				//Move to the gap
				case 3:
					if(leftRange < 0.2 || mleftRange < 0.2)
						moveForward(FORWARD_SPEED_SHIGH);
					else 
						stage = 4;
					break;
				//Turn towards left (from robot) obstacle of 2nd gap
				case 4:
					if(frontRange > 1.7)
						moveForwardRight(FORWARD_SPEED_HIGH, TURN_RIGHT_SPEED_LOW);
					else 
						stage = 5;
					break;
				//Approach 2nd gap
				case 5:
					if(frontRange > 0.65)
						moveForward(FORWARD_SPEED_SHIGH);
					else
						stage = 6;				
					break;
				//Turn towards inside of the 2nd gap
				case 6:
					if(mleftRange > 0.3)
						moveForwardRight(FORWARD_SPEED_HIGH, TURN_RIGHT_SPEED_MIDDLE);
					else 
						stage = 7;
					break;
				//Go through 2nd gap
				case 7:
					if(frontRange > 0.55)
						moveForward(FORWARD_SPEED_HIGH);
					else 
						stage = 8;
					break;
				//Move in arc trajectory towards charger
				case 8:
					if(frontRange > 0.25 && leftRange > 0.20)
						moveForwardRight(FORWARD_SPEED_HIGH, TURN_RIGHT_SPEED_LOW);
					else
						moveStop();
					break;
		
			}
		}
		transformMapPoint(laserMapFile, frontRange, frontAngle, robotHeadAngle, PositionX, PositionY);
		transformMapPoint(laserMapFile, mleftRange, mleftAngle, robotHeadAngle, PositionX, PositionY);
		transformMapPoint(laserMapFile, leftRange, leftAngle, robotHeadAngle, PositionX, PositionY);
		transformMapPoint(laserMapFile, rightRange, rightAngle, robotHeadAngle, PositionX, PositionY);
		transformMapPoint(laserMapFile, mrightRange, mrightAngle, robotHeadAngle, PositionX, PositionY);

		odomTrajFile << PositionX << " " << PositionY << endl;

		laserFile << i++ << " " << frontRange << " " << mleftRange << " " << leftRange << " " << rightRange << " " << mrightRange << endl;

		ros::spinOnce(); // Allow ROS to process incoming messages
		rate.sleep();  // Wait until defined time passes.
	}
	odomTrajFile.close();
	odomVelFile.close();
	laserFile.close();
	laserMapFile.close();
}



int main(int argc, char **argv) {
	ros::init(argc, argv, "stopper"); // Initiate new ROS node named "stopper"
	Stopper stopper; // Create new stopper object
	stopper.startMoving(); // Start the movement
	return 0;
}

