#!/usr/bin/env python3

"""
This program takes colour imagery from left and right cameras to 
calculate the distance to the objects on the images and locate UFO 
if such is present.

Expected invocation:
{

python3 <filename> <n_frames> <left_image> <right_image>

}

Typical invocation: 
{

python3 ce316ass.py 50 left-%3.3d.png right-%3.3d.png

}

Typical output:
{

frame  identity  distance
    0  <colour>    <dist>
...
   46  white     2.64e+07
   46  orange    2.19e+07
...
  n-1  <colour>    <dist>
UFO: cyan

}
where n is number of frames, <colour> is
one of the colours in the image, left aligned, <dist> is
a distance to the object in scientific e notation
that has been calculated using both images
"""
import sys, cv2, numpy, math

# Enables(True)/ Disables(False) debug messages
DEBUG = False

# Initialize dictionaries
#Colours
coloursHSV = {
	"red": [0, 255],
	"yellow": [30, 255],
	"blue": [120, 255],
	"green": [60, 255],
	"cyan": [90, 255],
	"white": [0, 0],
	"orange": [22, 255]
}

# Preallocate to store center coordinates of all
# aolour object that were found in the images
points = {
	"red": [],
	"yellow": [],
	"blue": [],
	"green": [],
	"cyan": [],
	"white": [],
	"orange": []
}

# Locate and store colour objects
"""
Function locates colours on the image and outputs them as dictionaries.
First, threshold the image in the range of a specified colours Hue and Saturation
but minimum value is 1 and maximum is 255.
The minimum value is not 0 to not mix up with black background.
After thresholding the only object on the image is ONE colour spot.
Find the contour of it to find the center of it.
"""
def locate(im):
	# Declare
	cluster = {}

	for colour, hsv in coloursHSV.items():
		t_low = (hsv[0], hsv[1], 1)
		t_high = (hsv[0], hsv[1], 255)
		
		# Threshold and locate
		bin_frame = cv2.inRange(im, t_low, t_high)
		contours, hierarchy =\
		cv2.findContours(bin_frame, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
		
		if len(contours) > 0:
			# Obtain contours
			cnt = contours[0]

			# fit minimum enclosing circle to find the center of it
			(x, y), r = cv2.minEnclosingCircle(cnt)
			center = [int(x), int(y)]
			
			# Store
			cluster[colour] = center
		
	return cluster
	

# Find distance away
"""
All constants are in metres

spacing - pixel spacing of the cameras
f - focal length of the cameras
B - base line, distance that cameras are apart 

Function calculates the horizonatal distance
from the center for both images and converts to meters
then calculate the distance Z using formula
"""
def distance(left, right, center):
	log_Z = {}
	# Constants (m)
	spacing = 1e-5
	f = 12
	B = 3500
	for colour in coloursHSV:
		if (colour in left) and (colour in right):
			# Horizonatal distance apart
			x_l = (left[colour][0] - center[0]) * spacing
			x_r = (right[colour][0] - center[0]) * spacing

			# Distance away
			Z = (f * B) / (x_l - x_r)
			
			# Store in dictionary
			log_Z[colour] = Z
			
			if DEBUG:
				z_str = printDist(str(int(Z)))
				print(colour, ":  ", z_str)
	if DEBUG:
		print("---------------------")
		
	return log_Z


# Reversed for-loop was adapted from
# https://stackoverflow.com/questions/3476732/how-to-loop-backwards-in-python 
# comment by Chris Flesher under accepted answer
# Print in a form of: 1'234'567'890
# Only used while DEBUG
def printDist(Z):
	prnt = ''
	ret = ''
	for c in range(len(Z) - 1, -1, -1):
		prnt += Z[c]
		if c < len(Z) - 1 and c > 0:
			if(len(Z)- c)%3 == 0:
				prnt += '\''
	
	for c in range(len(prnt) - 1, -1, -1):
		ret += prnt[c]
	
	return ret

    
# Approximate the min box of points
def fitRect(colour):
	# fit points inside the rectangle
	# convert to numpy array to make it readable to cv2
	points_numpy = numpy.array(points[colour]) 
	rect = cv2.minAreaRect(points_numpy)
	
	height = rect[1][1]
	width = rect[1][0]

	return height

# Build a string
def build_string(ufos):
	ufo_string = ""
	for ufo in ufos:
		ufo_string += ufo + " "
	return ufo_string
    

# Find UFO
def findUFO():
	"""
	The idea is to fit the trajectory of an object inside a rectangle.
	If the height of this box is greater than an arbitary error constant 
	this means that the object travelled in a non-linear trajectory, meaning
	it is a UFO
	"""
	ufos = []
	for colour in coloursHSV: 
		
		# obtain height of a minimum rectangle 
		height = fitRect(colour)
		if DEBUG:
			print("The ratio for colour: ", colour, " is ", height)
			print(colour, " is a UFO")
	
		if(height > 5):
			ufos.append(colour)
		
	return ufos
    

# Check input first  
if len (sys.argv) < 4:
	print("Usage: %s <n_frames> <left_images> <right_images>" %\
	sys.argv[0], file=sys.stderr)
	
	print("Typical invocation: python3 %s 50 left-%%3.3d.png right-%%3.3d.png" %\
	sys.argv[0], file=sys.stderr)
	
	exit(1)
	
	
# Read pictures and do search routine
# Main loop
print("frame  identity  distance")
nframes = int(sys.argv[1])
for frame in range(0, nframes):
	fn_left = sys.argv[2] % frame
	fn_right = sys.argv[3] % frame
	
	# Read images
	bgr_left = cv2.imread(fn_left)
	bgr_right = cv2.imread(fn_right)
	
	# Convert to HSV colour space
	hsv_left = cv2.cvtColor(bgr_left, cv2.COLOR_BGR2HSV)
	hsv_right = cv2.cvtColor(bgr_right, cv2.COLOR_BGR2HSV)
	
	# Search for object on the images and output them in the dictionary
	cluster_left = locate(hsv_left)
	cluster_right = locate(hsv_right)
	
	# Only use one cluster because it is enough
	for colour in cluster_left:
		# update positions for every colour
		points[colour].append(cluster_left[colour])
	
	if DEBUG:
		# Show images that are currently in work
		cv2.imshow("Left", bgr_left)
		cv2.imshow("Right", bgr_right)
		cv2.waitKey(100)
		cv2.destroyAllWindows()

	# Center of an image (x, y)
	im_center = (int(hsv_left.shape[0]/2), int(hsv_left.shape[1]/2))
	
	# Find distance away
	dist = distance(cluster_left, cluster_right, im_center)
	
	for colour, d in dist.items():
		# produce scientific notation
		d = "{:.2e}".format(d)
		# print information on every iteration for every colour
		print("%5d  %-8s  %8s" % (frame, colour, d))

# Find UFOs
ufos = findUFO()
print("UFO: ", build_string(ufos))



