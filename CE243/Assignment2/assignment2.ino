int servoRight = SERVO_0;   //Declare Servos
int servoLeft = SERVO_1;

int infraRight = IO_0;      //Declare Infrared Sensors
int infraLeft = IO_1;

int sonic = GPP_0;      //Declare Ultrasonic Sensors

void setup() {
  InitEmoro();                                 // Initializes all available inputs and outputs on EMoRo 2560.
 
  Serial.begin(9600);
  
  //Attach infrared sensors
  pinMode(infraRight, INPUT_PULLUP);          
  pinMode(infraLeft, INPUT_PULLUP);
  
  //Attach servos
  EmoroServo.attach(servoRight);             
  EmoroServo.attach(servoLeft);      

  //Attach ultrasonic sensors
  Ultrasonic.attach(sonic);
}

//Obstacle avoidance
void obst(){//-------------------------------------------------------------------------------------------Obst
  while(ReadSwitch(SW_4) == 0){
    Lcd.clear();
    Lcd.locate(0,0);
    Lcd.print("moving");
    Lcd.locate(1, 0);
    Lcd.print("forward.");
  
    int wait = 10;
    int speedR = 2500, speedL = 500; //speed
    int dis = Ultrasonic.read(sonic);//distance from ultrasonic sensor
 
    if(dis < 15){
      Lcd.clear();
      Lcd.locate(0,0);
      Lcd.print("obstacle");
      Lcd.locate(1,0);
      Lcd.print("detected.");
      //turn right
      speedR = 500;
      speedL = 500; 
      wait = 500;   
    }
    
    EmoroServo.write(servoRight, speedR);
    EmoroServo.write(servoLeft, speedL);
    delay(wait);
  }
}//------------------------------------------------------------------------------------------------------Obst

//Follow the line(CIRCLE)
void circle(){//-----------------------------------------------------------------------------------------Circle
  //For line following in a circle use car backwards,
  //therefore car goes backwards pretending its a front
  int left, right; //infrared readings
  
  Lcd.clear();
  Lcd.locate(0,0);
  Lcd.print("Cycle Track");
  Lcd.locate(1,0);
  Lcd.print("Following");
  
  while(ReadSwitch(SW_4) == 0){
    left = digitalRead(infraLeft);
    right = digitalRead(infraRight);
    int movement;
	
	  if(left == 0 && right == 0){
		  movement = 0; //forward
	  }else if(left == 1 && right == 0) {
		  movement = 1; //right
	  }else if(left == 0 && right == 1) {
		  movement = 2; //left
	  }else if(left == 1 && right == 1) {
		  movement = 3; //none
	  }
	
	  switch(movement){
		  case 0: //forward
			  EmoroServo.write(servoRight, 500);
			  EmoroServo.write(servoLeft, 2500);
			  delay(10);
			  break;
		  case 1: //right
			  EmoroServo.write(servoRight, 500);
			  EmoroServo.write(servoLeft, 1900);
			  delay(100);
			  break;
		  case 2: //left
			  EmoroServo.write(servoRight, 1100);
			  EmoroServo.write(servoLeft, 2500);
			  delay(100);
			  break;
		  case 3: //no black line (backwards)
			  EmoroServo.write(servoRight, 2500);
			  EmoroServo.write(servoLeft, 500);
			  delay(10);
			  break;
	  }
  }
}//------------------------------------------------------------------------------------------------------Circle

//Follow the line(SQUARE)
void square(){//-----------------------------------------------------------------------------------------Square
	//For line following in a circle use car backwards,
	//therefore car goes backwards pretending its a front
	int movement;  
	int count = 0; //how many times it turned
	int flag; //if == 0 then turn left, if == 1 then turn right
	int left, right; //infrared readings
  
	while(ReadSwitch(SW_4) == 0){
  
		left = digitalRead(infraLeft);
		right = digitalRead(infraRight);
    
		if(left == 0 && right == 0){
			movement = 0; //forward
		}else if(left == 1 && right == 0) {
			movement = 1; //right
		}else if(left == 0 && right == 1) {
			movement = 2; //left
		}else if(left == 1 && right == 1) {
			movement = 3; //none
		}
	
		switch(movement){
			case 0: //forward
				EmoroServo.write(servoRight, 500);
				EmoroServo.write(servoLeft, 2500);
				delay(10);
				Lcd.clear();
				Lcd.locate(0,0);
				Lcd.print("Square Track");
				Lcd.locate(1,0);
				Lcd.print("Following");
				break;
        
			case 1:	//right
				EmoroServo.write(servoRight, 500);
				EmoroServo.write(servoLeft, 2000);
				delay(50);
				break;
        
			case 2:	//left
				EmoroServo.write(servoRight, 1000);
				EmoroServo.write(servoLeft, 2500);
				delay(50);
				break;
        
			case 3:	//corner
				count++;
				if(count == 1){ 
					//go forward little bit more
					EmoroServo.write(servoRight, 500);
					EmoroServo.write(servoLeft, 2500);
					delay(130);

					//turn 90 deg right
					EmoroServo.write(servoRight, 500);
					EmoroServo.write(servoLeft, 500);
					delay(430);

					//needs to have stop to have time to take readings
					EmoroServo.write(servoRight, 1500);
					EmoroServo.write(servoLeft, 1500);
					delay(75);
          
					//take a reading
					left = digitalRead(infraLeft);
					right = digitalRead(infraRight);
          
					//if there is no black line...
					if(left && right){
						flag = 0; //left

						left = digitalRead(infraLeft);
						right = digitalRead(infraRight);
            
						//turn left until find blackline
						while(left && right){
							//turn 180 opposite direction to check if there is a line
							EmoroServo.write(servoRight, 2500);
							EmoroServo.write(servoLeft, 2500);
							delay(10);
            
							//take a reading again
							left = digitalRead(infraLeft);
							right = digitalRead(infraRight);
						}
				  
					//...but if there is a line (NAND operand)
					}else if(!(left && right)){
						flag = 1; //right
					}
					left = digitalRead(infraLeft);
					right = digitalRead(infraRight);
				}
        
				if(flag == 0){  //go anticlockwise(left)
					EmoroServo.write(servoRight, 2500);
					EmoroServo.write(servoLeft, 2500);
					delay(20);
          
					Lcd.clear();
					Lcd.locate(0,0);
					Lcd.print("Sharp turning");
					Lcd.locate(1,0);
					Lcd.print("-90 degrees.");
                    
				}else if(flag == 1){ //go clockwise(right)
					EmoroServo.write(servoRight, 500);
					EmoroServo.write(servoLeft, 500);
					delay(20);
        
					Lcd.clear();
					Lcd.locate(0,0);
					Lcd.print("Sharp turning");
					Lcd.locate(1,0);
					Lcd.print("90 degrees.");
				}
				break;
		}
	}
}//------------------------------------------------------------------------------------------------------Square

//Reset the conditions, go back to the menu
void reset(){//------------------------------------------------------------------------------------------Reset
	//wipe out LCD
	Lcd.clear();
  
	//Sets new speed to 0
	EmoroServo.write(servoRight, 1500);
	EmoroServo.write(servoLeft, 1500);
}//------------------------------------------------------------------------------------------------------Reset

//Printing the menu
void initPrint() {//-------------------------------------------------------------------------------------initPrint
	//Column 1
    Lcd.locate(0, 0);     //Row 1
    Lcd.print("1.Obst");
    Lcd.locate(1, 0);     //Row 2
    Lcd.print("3.Square");

    //Column 2
    Lcd.locate(0, 8);     //Row 1
    Lcd.print("2.Circle");    
    Lcd.locate(1, 8);     //Row 2
    Lcd.print("4.Reset");      
}//------------------------------------------------------------------------------------------------------initPrint

void loop() {
	initPrint();
  
	int button = 0;

	if(ReadSwitch(SW_1) == 1){
		button = 1;
	}else if(ReadSwitch(SW_2) == 1){
		button = 2;
	}else if(ReadSwitch(SW_3) == 1) {
		button = 3;
	}else if(ReadSwitch(SW_4) == 1) {
		button = 4;
	}
   
	switch(button) {
		case 1:
			obst();
			break;
		case 2:
			circle();
			break; 
		case 3:
			square();
			break;
		case 4:
			reset();
			break;
	}
}
