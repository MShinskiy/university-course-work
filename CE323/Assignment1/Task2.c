#include <Keypad.h>
#include <LiquidCrystal.h>

//Initial cursor position
int row;
int col;

//Password
const char code[4] = {'1', '2', '3', '4'}; 
//Guess
char guess[4];

//Keypad============
const byte ROWS = 4; 
const byte COLS = 4; 

char keys[ROWS][COLS] = {   
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};

byte rowPins[ROWS] = {11, 10, 9, 8}; 
byte colPins[COLS] = {7, 6, 5, 4}; 

Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);

// LCD===============
LiquidCrystal lcd(A0, A1, A2, A3, A4, A5);

// Prepare==================
void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);
  startup();
}

void startup(){
  lcd.clear();				//clear screen
  row = 1;					//set row and col to start position
  col = 0;			
  lcd.setCursor(col, row);
  lcd.print("Code:____");	//print start up message
}

//Main======================
void loop() {
  //retrieve key
  char key = keypad.getKey();
  
  
  if(key != NO_KEY){
    //press C to delete last char
    if(key == 'C'){
      //delete the message 
      //if last char deleted
      if(col > 3){
        lcd.setCursor(0, 0);
        lcd.print("                   ");
      }
      if(col > 0){
        //move cursor back
        col--;
      }
      //Set delete char in the array
      //and set cursor
      guess[col] = NULL;
      lcd.setCursor(col+5, row);
      lcd.print('_');
	
    } else {
      //until pasword is full
      if( col < 4){
      	lcd.setCursor(col+5, row);
        //put key into an array
        //and do the routine for LCD
      	guess[col] = key;
      	lcd.print('*');
        col++;
      }
      //print message if password fills
      if(col > 3){
      	lcd.setCursor(0, 0);
        lcd.print("Press B to set");
      }
      //when password is filled up
      //and 'B' pressed
      if(col == 4 && key == 'B'){
        //check password
        int check = 0;
        for(int x = 0; x < 4; x++){
          if(guess[x] == code[x]){
           check++; 
          }
        }
        //if correct
        if(check == 4){
          //do routine
          Serial.println("Correct!");
          lcd.setCursor(0,0);
          lcd.print("                   ");	//delete previous
          lcd.setCursor(0, 0);
          lcd.print("CE323 A1");			//set new
          //freeze for 5 seconds to show the results
          delay(5000);
        //if wrong
        } else {
          //do routine
          Serial.println("Wrong!");
        }
        //reset the screen and password
        startup();
      }
    }
    //print every key that was pressed
    Serial.println(key);
  }
}