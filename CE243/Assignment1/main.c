#include <stdio.h>
#include <string.h>
#include "stuffDef.h"

//created by Dr. Heinz Doofenshmirtz id: 1804336
//on 04-11-19
int main(void) {

    //Declare the variables
    int
        was,
        count = 0; ///was defines the previous state, count counts the lines
    char
        string[25],
        fName_read[40],
        fName_write[40];

    FILE *fpr = NULL; //file pointer for reading
    FILE *fpw = NULL; //file pointer for writing

    //open file         most of this part is taken from Lab 2, completed by me
    while(fpr == NULL) {

        //asking for input
        printf("Enter the name of the file to read: ");
        scanf("%s", fName_read);
        fpr = fopen(fName_read, "r");

        if(fpr != NULL) {
            //break if file found
            break;
        }
        printf("No such file exists in project directory\n");
    }
    printf("\n");

    //open/create a file to write       learned from: [1]
    printf("Enter the name of the file to write: ");
    scanf("%s", fName_write);
    fpw = fopen(fName_write, "w");
    printf("\n");

    //printing from file in while loop
    //writing to file
    while (fscanf(fpr, "%s", string) != EOF) {

        //Local variables declaration
        char
            newStr[25] = {0},
            localStr[25];

        int
            state = 0,
            len = strlen(string),
            newStrLen = strlen(newStr);

        ///Determine the state

        //Check for punctuation marks
        for (int x = 0; x < punctArrLen; x++) {
            if(string[len - 1] == punctArr[x]) {
                state = 1;
            }
        }

        //Check if there is a punct. mark after a keyword
        if(state == 1) {

            strcpy(localStr, string);

            /*
            quitting the loop by break keyword
            while loop needed for the case when
            there are few consequent punctuation marks
            after a keyword
            */
            while(1) {
                //local variables
                int
                    thereIs = 0,
                    localLen = strlen(localStr);

                //Erase last character (punctuation mark)
                for(int x = 0; x < localLen - 1; x++) {
                    newStr[x] = localStr[x];
                }

                newStrLen = strlen(newStr);

                //compares last character to an array of punct. marks
                for (int x = 0; x < punctArrLen; x++) {
                    if(newStr[newStrLen - 1] == punctArr[x]) {
                        thereIs = 1;
                        break; //break because only one match possible
                    }
                }

                //break if there is only one punct. mark
                if(thereIs == 0){
                    break;

                //if there is punct. mark left in a string, updates changes
                //and restores newStr
                } else if(thereIs == 1) {
                    strcpy(localStr, newStr);
                    memset(newStr,0,sizeof(newStr));
                }
            }

            //compares string to a keyword array
            for(int x = 0; x < keyArrLen; x++) {
                if(strcmp( newStr, keyArr[x]) == 0) {
                    state = 2;
                    break; //break because only one match possible
                }
            }

        } else {
        //Check if there is just a keyword
            for(int x = 0; x < keyArrLen; x++) {
                if(strcmp( string, keyArr[x]) == 0) {
                    state = 2;
                }
            }
        }

        switch(state) {
            case 1 :        ///state(case) 1: punct. mark

                count++;
                printf("%s \n", string);
                fprintf(fpw, "%s \n", string);
                was = 1;
                break;
            case 2 :        ///state(case) 2: keyword and punct. mark or keyword on its own

                if(was == 1) {
                    printf("%s ", string);
                    fprintf(fpw, "%s ", string);
                } else {
                    count++;
                    printf(" \n%s ", string);
                    fprintf(fpw, " \n%s ", string);
                }
                was = 2;
                break;
            default :
                if(was == 2){
                    count++;
                    printf(" \n%s ", string);
                    fprintf(fpw, " \n%s ", string);
                } else {
                    printf("%s ", string);
                    fprintf(fpw, "%s ", string);
                }
                was = 3;
        }
    }
    printf("\nTotal number of lines is: %d\n", count);
    fprintf(fpw, "\nTotal number of lines is: %d\n", count);

    //close files
    fclose(fpr);
    fclose(fpw);

    return 0;
}
///[1]https://www.tutorialspoint.com/cprogramming/c_file_io.htm
