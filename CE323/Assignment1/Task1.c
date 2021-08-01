//LEDs pins
int ledR = A0;
int ledO = A1;
int ledY = A2;
int ledG = A3;
int ledB = A4;
int ledW = A5;

//Store LEDs
int leds[6] = {
  ledR, 
  ledO, 
  ledY, 
  ledG,
  ledB,
  ledW
};

//store arr len
int size_leds = sizeof(leds)/sizeof(leds[0]);

//Switch pins
int switchR = 13;
int switchO = 12;
int switchY = 11;
int switchG = 10;
int switchB = 9;
int switchW = 8;

//Store LEDs
int switches[6] = {
  switchR,
  switchO,
  switchY,
  switchG,
  switchB,
  switchW
};

//store arr len
int size_switches = sizeof(switches)/sizeof(switches[0]);

void setup()
{
  //SERIAL output
  Serial.begin(9600);
 
  //initialize LEDs
  for(int led = 0; led < size_leds; led++){
    pinMode(leds[led], OUTPUT);
  }

  //initialize SWITCHs
  for(int swit = 0; swit < size_switches; swit++){
    pinMode(switches[swit], INPUT);
  }
  
}

void loop()
{

  //Read swtich state and store
  int state_switches[6] = {
    digitalRead(switchR),
    digitalRead(switchO), 
    digitalRead(switchY),
    digitalRead(switchG),
    digitalRead(switchB),
    digitalRead(switchW)
  };
  
  //Set LEDs according to switch state
  for(int led = 0; led < size_leds; led++){
    digitalWrite(leds[led], state_switches[led]);
    
  }
  
  for(int swit = 0; swit < size_switches; swit++){
    //if LED on
    if(state_switches[swit] == 1){
      //delay var
      int millis;
      
      //define delay in millis
      //for each LED
      switch (swit) {
        case 0:
          millis = 200;
          break;
        case 1:
          millis = 500;
          break;
        case 2:
          millis = 800;
          break;
        case 3:
          millis = 1000;
          break;
        case 4: 
          millis = 1500;
          break;
        case 5:
          millis = 2000;
          break;
      }
      //do blink
      delay(millis);
      digitalWrite(leds[swit], 0);
      delay(millis);
    }
  }
}
