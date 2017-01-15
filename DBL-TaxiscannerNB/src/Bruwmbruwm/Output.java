//Add to our package
//package Bruwmbruwm;

//This class will have methods for all Output commands...
//Other classes can call upon the methods of this class to write output to Taxiscanner
//All output from the methods is concatenated as one string in the end, and then sent to TaxiScanner as output for one 'minute'
public class Output{
    
     TaxiScanner taxiscanner = TaxiScanner.getInstance();
     
    //the string that holds all output for current minute, this will be send to Taxiscanner
    public String minuteOutput = "";

    public void pickUpPassenger(int taxi, int destination){
        // in the format p, taxi, destination node
        addToMinuteOutput("p "+ (taxi + 1) +" "+destination+" ");
    }

    public void dropOffPassenger(int taxi, int destination){
        //in the format d, taxi, destination node
        addToMinuteOutput("d "+ (taxi + 1) +" "+destination+" ");
    }

    public void taxiGoTo(int taxi, int destination){
        //in the format m, taxi, node to travel to
        //this is only possible if the taxi is adjacent to the destination node...
        //AKA you can only travel one edge at a time
        addToMinuteOutput("m "+ (taxi + 1) +" "+destination+" ");
    }

    //should only be used before initialization, use taxiGoTo afterwards
    public void taxiSetPosition(int taxi, int node){
        addToMinuteOutput("m "+ (taxi + 1) +" "+node+" ");
    }

    //adds a command to our output string minuteOutput for the current minute
    private void addToMinuteOutput(String output){
        minuteOutput = minuteOutput+output;
    }

    //sends output of the current minute to Taxiscanner
    //this will in turn start a new minute so input should be read after calling this
    //also clears minuteOutput
    public void sendOutput(){
        //write c to minuteoutput, this ends the current minute when read by peach
        addToMinuteOutput("c");
        taxiscanner.println(minuteOutput);
        minuteOutput = "";
    }
    //for testing purposes, this gives the output string
    public String getMinuteOutput(){
        return minuteOutput;
    }
}