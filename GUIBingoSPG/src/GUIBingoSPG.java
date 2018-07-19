
import java.util.HashSet;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUIBingoSPG extends Application
{
    private static Pane pane = new Pane();
        
    private static Text headingText = new Text();
    
    private static Button playButton = new Button("Play!");
    
    private static Button newNumberButton = new Button("Draw New Number");
    
    private static Button resetButton = new Button("Reset");
    
    private static String[] bingoCardTitleText = new String[] {"B", "I", "N", "G", "O"};   
    
    private static int[] player1_randomBingoNumbers = new int[25];
    private static int[] player2_randomBingoNumbers = new int[25];
    
    private static Text[] bingoCardNumbers_Player1_Text = new Text[25];    //Textual representation of the Bingo Card random ints
    private static Text[] bingoCardNumbers_Player2_Text = new Text[25];
    
    private static Text[] drawnBingoNumbers_Text = new Text[48];    //The bingo numbers that are drawn and displayed below Player 1's card
    private static int[] drawnBingoNumbers = new int[48];
    
    //The 2 lines that intersect to create an X for Player 1's Bingo Card
    private static Line[] line1_Player1 = new Line[25];
    private static Line[] line2_Player1 = new Line[25];
    
    //X for Player 2's Bingo Card
    private static Line[] line1_Player2 = new Line[25];
    private static Line[] line2_Player2 = new Line[25];
    
    //Tickers that become true when a matched number is found
    private static boolean[][] matchedNumberTicker_Player1 = new boolean[5][5];
    private static boolean[][] matchedNumberTicker_Player2 = new boolean[5][5];
    
    private static boolean player1Wins = false;
    private static boolean player2Wins = false;
    
    @Override
    public void start(Stage primaryStage) 
    {
        //Create the Bingo Cards themselves
        createHorizontalLinesForBingoCards();
        createVerticalLinesForBingoCards();
        
        //Create the BINGO letters
        placeBINGOLettersInCorrectSpot();
                
        //Create the random ints that go inside the Bingo Card blocks
        generateRandomInts_Player1BingoCard();
        generateRandomInts_Player2BingoCard();
                
        //Set the position of the random ints inside the Bingo Card blocks
        setRandomIntsPosition_Player1BingoCard();
        setRandomIntsPosition_Player2BingoCard();
        
        //Initialize the FREE blocks
        initializeFREESpots();
        
        //Generate the random numbers that will be drawn
        generateBingoNumbersToBeDrawn();
                
        Scene scene = new Scene(pane, 1200, 800);
        
        //Create Player1 Text
        Text player1_Text = new Text(200, 50, "Player 1");
        player1_Text.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 25));
        
        //Create Player2 Text
        Text player2_Text = new Text(900, 50, "Player 2");
        player2_Text.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 25));
        
        //Set Heading Text at startup
        headingText.setX(500);
        headingText.setY(50);
        headingText.setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 25));
        headingText.setText("Welcome To Bingo!");
        
        //Play Button
        playButton.setLayoutX(550);
        playButton.setLayoutY(125);
        playButton.setPrefSize(100, 50);
        playButton.setStyle("-fx-font-size: 2em;");
        playButton.setOnAction(new PlayButtonHandler());
        
        //Draw New Number Button
        newNumberButton.setLayoutX(490);
        newNumberButton.setLayoutY(225);
        newNumberButton.setPrefSize(225, 50);
        newNumberButton.setStyle("-fx-font-size: 1.5em;");
        newNumberButton.setOnAction(new DrawNewNumberHandler());
        newNumberButton.setDisable(true);
        
        //Reset Button
        resetButton.setLayoutX(550);
        resetButton.setLayoutY(325);
        resetButton.setPrefSize(100, 50);
        resetButton.setStyle("-fx-font-size: 2em;");
        resetButton.setOnAction(new ResetButtonHandler());
        resetButton.setDisable(true);
        
        pane.getChildren().addAll(player1_Text, player2_Text, headingText, playButton, newNumberButton, resetButton);
        
        
        primaryStage.setTitle("Bingo");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
    }

    public static void main(String[] args) 
    {
        GUIBingoSPG bingoSPG = new GUIBingoSPG();
                
        launch(args);
    }
    
    private static void createHorizontalLinesForBingoCards ()
    {
        //Creates the Horizontal Lines for both Bingo Cards
        
        int horizontalLinesYSpacing = 0;
        for (int l = 0; l < 7; l++)
        {
            Line player1_BingoHorizontalLines = new Line(50, (100 + horizontalLinesYSpacing), 425, (100 + horizontalLinesYSpacing));
            player1_BingoHorizontalLines.setStrokeWidth(2.5f);
            pane.getChildren().add(player1_BingoHorizontalLines);
            horizontalLinesYSpacing += 50;
        }
        
        horizontalLinesYSpacing = 0;
        for (int l = 0; l < 7; l++)
        {
            Line player2_BingoHorizontalLines = new Line(775, (100 + horizontalLinesYSpacing), 1150, (100 + horizontalLinesYSpacing));
            player2_BingoHorizontalLines.setStrokeWidth(2.5f);
            pane.getChildren().add(player2_BingoHorizontalLines);
            horizontalLinesYSpacing += 50;
        }
    }
    
    private static void createVerticalLinesForBingoCards ()
    {
        //Creates the Vertical lines for both Bingo Cards
        
        int verticalLineXSpacing = 0;
        for (int l = 0; l < 6; l++)
        {
            Line player1_BingoVerticalLines = new Line((50 + verticalLineXSpacing), 100, (50 + verticalLineXSpacing), 400);
            player1_BingoVerticalLines.setStrokeWidth(2.5f);
            pane.getChildren().add(player1_BingoVerticalLines);
            verticalLineXSpacing += 75;
        }
        
        verticalLineXSpacing = 0;
        for (int l = 0; l < 6; l++)
        {
            Line player2_BingoVerticalLines = new Line((775 + verticalLineXSpacing), 100, (775 + verticalLineXSpacing), 400);
            player2_BingoVerticalLines.setStrokeWidth(2.5f);
            pane.getChildren().add(player2_BingoVerticalLines);
            verticalLineXSpacing += 75;
        }
    }
    
    private static void placeBINGOLettersInCorrectSpot ()
    {        
        //Place B I N G O Characters in correct boxes for both cards
        
        int bingoLetterSpacing = 0;
        for (int c = 0; c < bingoCardTitleText.length; c++)
        {
            Text bingoText = new Text((72.5f + bingoLetterSpacing), 140, bingoCardTitleText[c]);
            if (c == 1)
            {
                bingoText.setX(155);
            }
            bingoText.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 40));
            pane.getChildren().add(bingoText);
            bingoLetterSpacing += 75;
        }
        
        bingoLetterSpacing = 0;
        for (int c = 0; c < bingoCardTitleText.length; c++)
        {
            Text bingoText = new Text((797.5f + bingoLetterSpacing), 140, bingoCardTitleText[c]);
            if (c == 1)
            {
                bingoText.setX(880);
            }
            bingoText.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 40));
            pane.getChildren().add(bingoText);
            bingoLetterSpacing += 75;
        }
    }
        
    private static void generateRandomInts_Player1BingoCard ()
    {
        //Generate 25 non-repeating random ints for Player 1's Bingo Card
        
        HashSet<Integer> bingoCardHashSet_Player1 = new HashSet<>();
        for (int n = 0; n < player1_randomBingoNumbers.length; n++) 
        {           
            if (n < 5)
            {
                int randomBingoCardInt = (int)(1 + Math.random() * 15); //this is the int we are adding
                while (bingoCardHashSet_Player1.contains(randomBingoCardInt)) 
                {   //while we have already used the number
                    randomBingoCardInt = (int) (1 + Math.random() * 15); //generate a new one because it's already used
                }
                bingoCardHashSet_Player1.add(randomBingoCardInt);
                player1_randomBingoNumbers[n] = randomBingoCardInt;
            }
            else if (n >= 5 && n < 10)
            {
                int randomBingoCardInt = (int)(16 + Math.random() * 15); //this is the int we are adding
                while (bingoCardHashSet_Player1.contains(randomBingoCardInt)) 
                {   //while we have already used the number
                    randomBingoCardInt = (int) (16 + Math.random() * 15); //generate a new one because it's already used
                }
                bingoCardHashSet_Player1.add(randomBingoCardInt);
                player1_randomBingoNumbers[n] = randomBingoCardInt;
            }
            else if (n >= 10 && n < 15)
            {
                int randomBingoCardInt = (int)(31 + Math.random() * 15); //this is the int we are adding
                while (bingoCardHashSet_Player1.contains(randomBingoCardInt)) 
                {   //while we have already used the number
                    randomBingoCardInt = (int) (31 + Math.random() * 15); //generate a new one because it's already used
                }
                bingoCardHashSet_Player1.add(randomBingoCardInt);
                player1_randomBingoNumbers[n] = randomBingoCardInt;
            }
            else if (n >= 15 && n < 20)
            {
                int randomBingoCardInt = (int)(46 + Math.random() * 15); //this is the int we are adding
                while (bingoCardHashSet_Player1.contains(randomBingoCardInt)) 
                {   //while we have already used the number
                    randomBingoCardInt = (int) (46 + Math.random() * 15); //generate a new one because it's already used
                }
                bingoCardHashSet_Player1.add(randomBingoCardInt);
                player1_randomBingoNumbers[n] = randomBingoCardInt;
            }
            else if (n >= 20 && n < 25)
            {
                int randomBingoCardInt = (int)(61 + Math.random() * 15); //this is the int we are adding
                while (bingoCardHashSet_Player1.contains(randomBingoCardInt)) 
                {   //while we have already used the number
                    randomBingoCardInt = (int) (61 + Math.random() * 15); //generate a new one because it's already used
                }
                bingoCardHashSet_Player1.add(randomBingoCardInt);
                player1_randomBingoNumbers[n] = randomBingoCardInt;
            }
        }
    }
    
    private static void generateRandomInts_Player2BingoCard ()
    {
        //Generate 25 non-repeating random ints for Player 2's Bingo Card
        
        HashSet<Integer> bingoCardHashSet_Player1 = new HashSet<>();
        for (int n = 0; n < player2_randomBingoNumbers.length; n++) 
        {           
            if (n < 5)
            {
                int randomBingoCardInt = (int)(1 + Math.random() * 15); //this is the int we are adding
                while (bingoCardHashSet_Player1.contains(randomBingoCardInt)) 
                {   //while we have already used the number
                    randomBingoCardInt = (int) (1 + Math.random() * 15); //generate a new one because it's already used
                }
                bingoCardHashSet_Player1.add(randomBingoCardInt);
                player2_randomBingoNumbers[n] = randomBingoCardInt;
            }
            else if (n >= 5 && n < 10)
            {
                int randomBingoCardInt = (int)(16 + Math.random() * 15); //this is the int we are adding
                while (bingoCardHashSet_Player1.contains(randomBingoCardInt)) 
                {   //while we have already used the number
                    randomBingoCardInt = (int) (16 + Math.random() * 15); //generate a new one because it's already used
                }
                bingoCardHashSet_Player1.add(randomBingoCardInt);
                player2_randomBingoNumbers[n] = randomBingoCardInt;
            }
            else if (n >= 10 && n < 15)
            {
                int randomBingoCardInt = (int)(31 + Math.random() * 15); //this is the int we are adding
                while (bingoCardHashSet_Player1.contains(randomBingoCardInt)) 
                {   //while we have already used the number
                    randomBingoCardInt = (int) (31 + Math.random() * 15); //generate a new one because it's already used
                }
                bingoCardHashSet_Player1.add(randomBingoCardInt);
                player2_randomBingoNumbers[n] = randomBingoCardInt;
            }
            else if (n >= 15 && n < 20)
            {
                int randomBingoCardInt = (int)(46 + Math.random() * 15); //this is the int we are adding
                while (bingoCardHashSet_Player1.contains(randomBingoCardInt)) 
                {   //while we have already used the number
                    randomBingoCardInt = (int) (46 + Math.random() * 15); //generate a new one because it's already used
                }
                bingoCardHashSet_Player1.add(randomBingoCardInt);
                player2_randomBingoNumbers[n] = randomBingoCardInt;
            }
            else if (n >= 20 && n < 25)
            {
                int randomBingoCardInt = (int)(61 + Math.random() * 15); //this is the int we are adding
                while (bingoCardHashSet_Player1.contains(randomBingoCardInt)) 
                {   //while we have already used the number
                    randomBingoCardInt = (int) (61 + Math.random() * 15); //generate a new one because it's already used
                }
                bingoCardHashSet_Player1.add(randomBingoCardInt);
                player2_randomBingoNumbers[n] = randomBingoCardInt;
            }
        }
    }
    
    private static void initializeFREESpots ()
    {
        //Intialize FREE cell for Player 1 
        
        matchedNumberTicker_Player1[2][2] = true;
        //create X
        Line line1ForFree_Player1 = new Line(bingoCardNumbers_Player1_Text[12].getX(),
                              bingoCardNumbers_Player1_Text[12].getY() - 20,
                              bingoCardNumbers_Player1_Text[12].getX() + 60,
                              bingoCardNumbers_Player1_Text[12].getY() + 7.5);
        line1ForFree_Player1.setStrokeWidth(2);
        Line line2ForFree_Player1 = new Line(bingoCardNumbers_Player1_Text[12].getX(),
                              bingoCardNumbers_Player1_Text[12].getY() + 7.5,
                              bingoCardNumbers_Player1_Text[12].getX() + 60,
                              bingoCardNumbers_Player1_Text[12].getY() - 20);
        line2ForFree_Player1.setStrokeWidth(2);
        pane.getChildren().addAll(line1ForFree_Player1, line2ForFree_Player1);
                
        //Intialize FREE cell for Player 2 
        
        matchedNumberTicker_Player2[2][2] = true;
        //create X
        Line line1ForFree_Player2 = new Line(bingoCardNumbers_Player2_Text[12].getX(),
                              bingoCardNumbers_Player2_Text[12].getY() - 20,
                              bingoCardNumbers_Player2_Text[12].getX() + 60,
                              bingoCardNumbers_Player2_Text[12].getY() + 7.5);
        line1ForFree_Player2.setStrokeWidth(2);
        Line line2ForFree_Player2 = new Line(bingoCardNumbers_Player2_Text[12].getX(),
                              bingoCardNumbers_Player2_Text[12].getY() + 7.5,
                              bingoCardNumbers_Player2_Text[12].getX() + 60,
                              bingoCardNumbers_Player2_Text[12].getY() - 20);
        line2ForFree_Player2.setStrokeWidth(2);
        pane.getChildren().addAll(line1ForFree_Player2, line2ForFree_Player2);
    }
    
    private static void setRandomIntsPosition_Player1BingoCard ()
    {         
        //This places the random ints in their appropriate position
        
        int blocksNumberSpacing_X = 0;
        int blocksNumberSpacing_Y = 0;
        
        for (int n = 0; n < bingoCardNumbers_Player1_Text.length; n++)
        {          
            Text numbers = new Text();
            
            if (n != 12)
            {
                numbers.setText(player1_randomBingoNumbers[n] + "");
            }
            else if (n == 12)
            {
                numbers.setText("FREE");
            }
            numbers.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 30));
            if (n < 5)
            {
                numbers.setX(80);
                numbers.setY(180 + blocksNumberSpacing_Y);
                if (n == 4)
                {
                    blocksNumberSpacing_X += 75;
                }
            }
            else if (n >= 5 && n < 10)
            {
                if (n == 5)
                {
                    blocksNumberSpacing_Y = 0;
                }
                numbers.setX((80 + blocksNumberSpacing_X));
                numbers.setY(180 + blocksNumberSpacing_Y);
                if (n == 9)
                {
                    blocksNumberSpacing_X += 75;
                }
            }
            else if (n >= 10 && n < 15)
            {
                if (n == 10)
                {
                    blocksNumberSpacing_Y = 0;
                }
                if (n != 12)
                {
                    numbers.setX((75 + blocksNumberSpacing_X));
                    numbers.setY(180 + blocksNumberSpacing_Y);
                }
                else if (n == 12)
                {
                    numbers.setX((205));
                    numbers.setY(180 + blocksNumberSpacing_Y);
                    numbers.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 25));
                }
                if (n == 14)
                {
                    blocksNumberSpacing_X += 75;
                }
            }
            else if (n >= 15 && n < 20)
            {
                if (n == 15)
                {
                    blocksNumberSpacing_Y = 0;
                }
                numbers.setX((75 + blocksNumberSpacing_X));
                numbers.setY(180 + blocksNumberSpacing_Y);
                if (n == 19)
                {
                    blocksNumberSpacing_X += 75;
                }
            }
            else if (n >= 20 && n < 25)
            {
                if (n == 20)
                {
                    blocksNumberSpacing_Y = 0;
                }
                numbers.setX((75 + blocksNumberSpacing_X));
                numbers.setY(180 + blocksNumberSpacing_Y);
            }
            bingoCardNumbers_Player1_Text[n] = numbers;
            pane.getChildren().add(numbers);
            blocksNumberSpacing_Y += 50;
        }
    }
    
    private static void setRandomIntsPosition_Player2BingoCard ()
    {
        //This places the random ints in their appropriate position
        
        int blocksNumberSpacing_X = 0;
        int blocksNumberSpacing_Y = 0;
        
        for (int n = 0; n < bingoCardNumbers_Player2_Text.length; n++)
        {          
            Text numbers = new Text();
            
            if (n != 12)
            {
                numbers.setText(player2_randomBingoNumbers[n] + "");
            }
            else if (n == 12)
            {
                numbers.setText("FREE");
            }
            numbers.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 30));
            if (n < 5)
            {
                numbers.setX(805);
                numbers.setY(180 + blocksNumberSpacing_Y);
                if (n == 4)
                {
                    blocksNumberSpacing_X += 75;
                }
            }
            else if (n >= 5 && n < 10)
            {
                if (n == 5)
                {
                    blocksNumberSpacing_Y = 0;
                }
                numbers.setX((805 + blocksNumberSpacing_X));
                numbers.setY(180 + blocksNumberSpacing_Y);
                if (n == 9)
                {
                    blocksNumberSpacing_X += 75;
                }
            }
            else if (n >= 10 && n < 15)
            {
                if (n == 10)
                {
                    blocksNumberSpacing_Y = 0;
                }
                numbers.setX((800 + blocksNumberSpacing_X));
                numbers.setY(180 + blocksNumberSpacing_Y);
                if (n != 12)
                {
                    numbers.setX((800 + blocksNumberSpacing_X));
                    numbers.setY(180 + blocksNumberSpacing_Y);
                }
                else if (n == 12)
                {
                    numbers.setX(930);
                    numbers.setY(180 + blocksNumberSpacing_Y);
                    numbers.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 25));
                }
                if (n == 14)
                {
                    blocksNumberSpacing_X += 75;
                }
            }
            else if (n >= 15 && n < 20)
            {
                if (n == 15)
                {
                    blocksNumberSpacing_Y = 0;
                }
                numbers.setX((800 + blocksNumberSpacing_X));
                numbers.setY(180 + blocksNumberSpacing_Y);
                if (n == 19)
                {
                    blocksNumberSpacing_X += 75;
                }
            }
            else if (n >= 20 && n < 25)
            {
                if (n == 20)
                {
                    blocksNumberSpacing_Y = 0;
                }
                numbers.setX((800 + blocksNumberSpacing_X));
                numbers.setY(180 + blocksNumberSpacing_Y);
            }
            bingoCardNumbers_Player2_Text[n] = numbers;
            pane.getChildren().add(numbers);
            blocksNumberSpacing_Y += 50;
        }
    }
    
    private static void generateBingoNumbersToBeDrawn ()
    {        
        //Generate the numbers that will be drawn along with the corresponding "B I N G O" letter
        
        HashSet<Integer> bingoNumberHashSet = new HashSet<>();
        for (int i = 0; i < drawnBingoNumbers_Text.length; i++) 
        {            
            int bingoNumber = (int)(1 + Math.random() * 75); //this is the int we are adding
            while (bingoNumberHashSet.contains(bingoNumber)) 
            {   //while we have already used the number
                bingoNumber = (int) (1 + Math.random() * 75); //generate a new one because it's already used
            }
            String drawnRandomBINGOCharacter = null;
            
            if (bingoNumber >= 1 && bingoNumber <= 15)
            {
                drawnRandomBINGOCharacter = "B";
            }
            else if (bingoNumber >= 16 && bingoNumber <= 30)
            {
                drawnRandomBINGOCharacter = "I";
            }
            else if (bingoNumber >= 31 && bingoNumber <= 45)
            {
                drawnRandomBINGOCharacter = "N";
            }
            else if (bingoNumber >= 46 && bingoNumber <= 60)
            {
                drawnRandomBINGOCharacter = "G";
            }
            else if (bingoNumber >= 61 && bingoNumber <= 75)
            {
                drawnRandomBINGOCharacter = "O";
            }
            bingoNumberHashSet.add(bingoNumber);
            drawnBingoNumbers_Text[i] = new Text(drawnRandomBINGOCharacter + bingoNumber);
            
            //add them to the int array containg the Drawn Bingo Numbers
            drawnBingoNumbers[i] = bingoNumber;
        }
    }
        
    private static int counter = 0;
    private static int yValueIncrementor = 0;
    private static void drawAndDisplayNewNumber ()
    { 
        //Display the drawn number onto the screen in rows of 10
        
        drawnBingoNumbers_Text[counter].setFont(Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 25));
        
        if (counter < 10)
        {
        drawnBingoNumbers_Text[counter].setX(50);
        drawnBingoNumbers_Text[counter].setY(450 + yValueIncrementor);
        pane.getChildren().add(drawnBingoNumbers_Text[counter]);
        }        
        else if (counter >= 10 && counter <= 19)
        {
            if (counter == 10)
            {
                yValueIncrementor = 0;
            }
            drawnBingoNumbers_Text[counter].setX(150);
            drawnBingoNumbers_Text[counter].setY(450 + yValueIncrementor);
            pane.getChildren().add(drawnBingoNumbers_Text[counter]);
        }
        else if (counter >= 20 && counter <= 29)
        {
            if (counter == 20)
            {
                yValueIncrementor = 0;
            }
            drawnBingoNumbers_Text[counter].setX(250);
            drawnBingoNumbers_Text[counter].setY(450 + yValueIncrementor);
            pane.getChildren().add(drawnBingoNumbers_Text[counter]);
        }
        else if (counter >= 30 && counter <= 39)
        {
            if (counter == 30)
            {
                yValueIncrementor = 0;
            }
            drawnBingoNumbers_Text[counter].setX(350);
            drawnBingoNumbers_Text[counter].setY(450 + yValueIncrementor);
            pane.getChildren().add(drawnBingoNumbers_Text[counter]);
        }
        else if (counter >= 40 && counter <= 47)
        {
            if (counter == 40)
            {
                yValueIncrementor = 0;
            }
            drawnBingoNumbers_Text[counter].setX(450);
            drawnBingoNumbers_Text[counter].setY(450 + yValueIncrementor);
            pane.getChildren().add(drawnBingoNumbers_Text[counter]);
        }       
        counter++;
        yValueIncrementor += 30; 
                
    }
    
    private static int counter_CheckForPlayer1Matches = 0;
    private static void checkForMatchedNumberAndWinner_Player1 ()
    { 
        //Check for matches for every cell block except for cell 12- that is the FREE block
        
        boolean numberInFreeBlockWasMatched = false;
        
        for (int n = 0; n < player1_randomBingoNumbers.length; n++)
        {
            if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[n])
            {                
                if (player1_randomBingoNumbers[n] <= 15)
                {
                    if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[0])
                    {
                        matchedNumberTicker_Player1[0][0] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[1])
                    {
                        matchedNumberTicker_Player1[1][0] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[2])
                    {
                        matchedNumberTicker_Player1[2][0] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[3])
                    {
                        matchedNumberTicker_Player1[3][0] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[4])
                    {
                        matchedNumberTicker_Player1[4][0] = true;
                    }
                }
                else if (player1_randomBingoNumbers[n] > 15 && player1_randomBingoNumbers[n] <= 30)
                {
                    if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[5])
                    {
                        matchedNumberTicker_Player1[0][1] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[6])
                    {
                        matchedNumberTicker_Player1[1][1] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[7])
                    {
                        matchedNumberTicker_Player1[2][1] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[8])
                    {
                        matchedNumberTicker_Player1[3][1] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[9])
                    {
                        matchedNumberTicker_Player1[4][1] = true;
                    }
                }
                else if (player1_randomBingoNumbers[n] > 30 && player1_randomBingoNumbers[n] <= 45) //12 is the FREE
                {
                    if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[10])
                    {
                        matchedNumberTicker_Player1[0][2] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[11])
                    {
                        matchedNumberTicker_Player1[1][2] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[12])
                    {
                        //Don't drawn another X
                        numberInFreeBlockWasMatched = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[13])
                    {
                        matchedNumberTicker_Player1[3][2] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[14])
                    {
                        matchedNumberTicker_Player1[4][2] = true;
                    }
                }
                else if (player1_randomBingoNumbers[n] > 45 && player1_randomBingoNumbers[n] <= 60)
                {
                    if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[15])
                    {
                        matchedNumberTicker_Player1[0][3] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[16])
                    {
                        matchedNumberTicker_Player1[1][3] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[17])
                    {
                        matchedNumberTicker_Player1[2][3] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[18])
                    {
                        matchedNumberTicker_Player1[3][3] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[19])
                    {
                        matchedNumberTicker_Player1[4][3] = true;
                    }
                }
                else if (player1_randomBingoNumbers[n] > 60 && player1_randomBingoNumbers[n] <= 75)
                {
                    if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[20])
                    {
                        matchedNumberTicker_Player1[0][4] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[21])
                    {
                        matchedNumberTicker_Player1[1][4] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[22])
                    {
                        matchedNumberTicker_Player1[2][4] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[23])
                    {
                        matchedNumberTicker_Player1[3][4] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer1Matches] == player1_randomBingoNumbers[24])
                    {
                        matchedNumberTicker_Player1[4][4] = true;
                    }
                }
                if (!numberInFreeBlockWasMatched)
                {
                    //create X
                    Line line1 = new Line(bingoCardNumbers_Player1_Text[n].getX() - 15,
                                      bingoCardNumbers_Player1_Text[n].getY() - 20,
                                      bingoCardNumbers_Player1_Text[n].getX() + 40,
                                      bingoCardNumbers_Player1_Text[n].getY() + 7.5);
                    line1.setStrokeWidth(2);
                    Line line2 = new Line(bingoCardNumbers_Player1_Text[n].getX() - 15,
                                      bingoCardNumbers_Player1_Text[n].getY() + 7.5,
                                      bingoCardNumbers_Player1_Text[n].getX() + 40,
                                      bingoCardNumbers_Player1_Text[n].getY() - 20);
                    line2.setStrokeWidth(2);
                
                    line1_Player1[n] = line1;
                    line2_Player1[n] = line2;
                
                    pane.getChildren().addAll(line1, line2);
                }
                numberInFreeBlockWasMatched = false;
            }
        }
        
        //check if player 1 has won
        //Horizonal rows
        if (matchedNumberTicker_Player1[0][0] && matchedNumberTicker_Player1[0][1] && matchedNumberTicker_Player1[0][2]
            && matchedNumberTicker_Player1[0][3] && matchedNumberTicker_Player1[0][4])
        {
            player1Wins = true;
        }
        else if (matchedNumberTicker_Player1[1][0] && matchedNumberTicker_Player1[1][1] && matchedNumberTicker_Player1[1][2]
            && matchedNumberTicker_Player1[1][3] && matchedNumberTicker_Player1[1][4])
        {
            player1Wins = true;
        }
        else if (matchedNumberTicker_Player1[2][0] && matchedNumberTicker_Player1[2][1] && matchedNumberTicker_Player1[2][2]
            && matchedNumberTicker_Player1[2][3] && matchedNumberTicker_Player1[2][4])
        {
            player1Wins = true;
        }
        else if (matchedNumberTicker_Player1[3][0] && matchedNumberTicker_Player1[3][1] && matchedNumberTicker_Player1[3][2]
            && matchedNumberTicker_Player1[3][3] && matchedNumberTicker_Player1[3][4])
        {
            player1Wins = true;
        }
        else if (matchedNumberTicker_Player1[4][0] && matchedNumberTicker_Player1[4][1] && matchedNumberTicker_Player1[4][2]
            && matchedNumberTicker_Player1[4][3] && matchedNumberTicker_Player1[4][4])
        {
            player1Wins = true;
        }
        //Vertical columns
        else if (matchedNumberTicker_Player1[0][0] && matchedNumberTicker_Player1[1][0] && matchedNumberTicker_Player1[2][0]
            && matchedNumberTicker_Player1[3][0] && matchedNumberTicker_Player1[4][0])
        {
            player1Wins = true;
        }
        else if (matchedNumberTicker_Player1[0][1] && matchedNumberTicker_Player1[1][1] && matchedNumberTicker_Player1[2][1]
            && matchedNumberTicker_Player1[3][1] && matchedNumberTicker_Player1[4][1])
        {
            player1Wins = true;
        }
        else if (matchedNumberTicker_Player1[0][2] && matchedNumberTicker_Player1[1][2] && matchedNumberTicker_Player1[2][2]
            && matchedNumberTicker_Player1[3][2] && matchedNumberTicker_Player1[4][2])
        {
            player1Wins = true;
        }
        else if (matchedNumberTicker_Player1[0][3] && matchedNumberTicker_Player1[1][3] && matchedNumberTicker_Player1[2][3]
            && matchedNumberTicker_Player1[3][3] && matchedNumberTicker_Player1[4][3])
        {
            player1Wins = true;
        }
        else if (matchedNumberTicker_Player1[0][4] && matchedNumberTicker_Player1[1][4] && matchedNumberTicker_Player1[2][4]
            && matchedNumberTicker_Player1[3][4] && matchedNumberTicker_Player1[4][4])
        {
            player1Wins = true;
        }
        //Diagonal
        else if (matchedNumberTicker_Player1[0][0] && matchedNumberTicker_Player1[1][1] && matchedNumberTicker_Player1[2][2]
            && matchedNumberTicker_Player1[3][3] && matchedNumberTicker_Player1[4][4])
        {
            player1Wins = true;
        }
        else if (matchedNumberTicker_Player1[0][4] && matchedNumberTicker_Player1[1][3] && matchedNumberTicker_Player1[2][2]
            && matchedNumberTicker_Player1[3][1] && matchedNumberTicker_Player1[4][0])
        {
            player1Wins = true;
        }
        //Four corners
        else if (matchedNumberTicker_Player1[0][0] && matchedNumberTicker_Player1[0][4]
            && matchedNumberTicker_Player1[4][0] && matchedNumberTicker_Player1[4][4])
        {
            player1Wins = true;
        }
        counter_CheckForPlayer1Matches++;
    }
    
    private static int counter_CheckForPlayer2Matches = 0;
    private static void checkForMatchedNumberAndWinner_Player2 ()
    {        
        //Check for matches for every cell block except for cell 12- that is the FREE block
        
        boolean numberInFreeBlockWasMatched = false;
        
        for (int n = 0; n < player2_randomBingoNumbers.length; n++)
        {
            if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[n])
            {                
                if (player2_randomBingoNumbers[n] <= 15)
                {
                    if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[0])
                    {
                        matchedNumberTicker_Player2[0][0] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[1])
                    {
                        matchedNumberTicker_Player2[1][0] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[2])
                    {
                        matchedNumberTicker_Player2[2][0] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[3])
                    {
                        matchedNumberTicker_Player2[3][0] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[4])
                    {
                        matchedNumberTicker_Player2[4][0] = true;
                    }
                }
                else if (player2_randomBingoNumbers[n] > 15 && player2_randomBingoNumbers[n] <= 30)
                {
                    if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[5])
                    {
                        matchedNumberTicker_Player2[0][1] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[6])
                    {
                        matchedNumberTicker_Player2[1][1] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[7])
                    {
                        matchedNumberTicker_Player2[2][1] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[8])
                    {
                        matchedNumberTicker_Player2[3][1] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[9])
                    {
                        matchedNumberTicker_Player2[4][1] = true;
                    }
                }
                else if (player2_randomBingoNumbers[n] > 30 && player2_randomBingoNumbers[n] <= 45)
                {
                    if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[10])
                    {
                        matchedNumberTicker_Player2[0][2] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[11])
                    {
                        matchedNumberTicker_Player2[1][2] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[12])
                    {
                        numberInFreeBlockWasMatched = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[13])
                    {
                        matchedNumberTicker_Player2[3][2] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[14])
                    {
                        matchedNumberTicker_Player2[4][2] = true;
                    }
                }
                else if (player2_randomBingoNumbers[n] > 45 && player2_randomBingoNumbers[n] <= 60)
                {
                    if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[15])
                    {
                        matchedNumberTicker_Player2[0][3] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[16])
                    {
                        matchedNumberTicker_Player2[1][3] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[17])
                    {
                        matchedNumberTicker_Player2[2][3] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[18])
                    {
                        matchedNumberTicker_Player2[3][3] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[19])
                    {
                        matchedNumberTicker_Player2[4][3] = true;
                    }
                }
                else if (player2_randomBingoNumbers[n] > 60 && player2_randomBingoNumbers[n] <= 75)
                {
                    if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[20])
                    {
                        matchedNumberTicker_Player2[0][4] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[21])
                    {
                        matchedNumberTicker_Player2[1][4] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[22])
                    {
                        matchedNumberTicker_Player2[2][4] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[23])
                    {
                        matchedNumberTicker_Player2[3][4] = true;
                    }
                    else if (drawnBingoNumbers[counter_CheckForPlayer2Matches] == player2_randomBingoNumbers[24])
                    {
                        matchedNumberTicker_Player2[4][4] = true;
                    }
                }
                
                if (!numberInFreeBlockWasMatched)
                {
                    //create X
                    Line line1 = new Line(bingoCardNumbers_Player2_Text[n].getX() - 15,
                                      bingoCardNumbers_Player2_Text[n].getY() - 20,
                                      bingoCardNumbers_Player2_Text[n].getX() + 40,
                                      bingoCardNumbers_Player2_Text[n].getY() + 7.5);
                    line1.setStrokeWidth(2);
                    Line line2 = new Line(bingoCardNumbers_Player2_Text[n].getX() - 15,
                                      bingoCardNumbers_Player2_Text[n].getY() + 7.5,
                                      bingoCardNumbers_Player2_Text[n].getX() + 40,
                                      bingoCardNumbers_Player2_Text[n].getY() - 20);
                    line2.setStrokeWidth(2);
                
                    line1_Player2[n] = line1;
                    line2_Player2[n] = line2;
                        
                    pane.getChildren().addAll(line1, line2);
                }
                numberInFreeBlockWasMatched = true;
            }
        }
        
        //check if player 2 has won
        //Horizonal rows
        if (matchedNumberTicker_Player2[0][0] && matchedNumberTicker_Player2[0][1] && matchedNumberTicker_Player2[0][2]
            && matchedNumberTicker_Player2[0][3] && matchedNumberTicker_Player2[0][4])
        {
            player2Wins = true;
        }
        else if (matchedNumberTicker_Player2[1][0] && matchedNumberTicker_Player2[1][1] && matchedNumberTicker_Player2[1][2]
            && matchedNumberTicker_Player2[1][3] && matchedNumberTicker_Player2[1][4])
        {
            player2Wins = true;
        }
        else if (matchedNumberTicker_Player2[2][0] && matchedNumberTicker_Player2[2][1] && matchedNumberTicker_Player2[2][2]
            && matchedNumberTicker_Player2[2][3] && matchedNumberTicker_Player2[2][4])
        {
            player2Wins = true;
        }
        else if (matchedNumberTicker_Player2[3][0] && matchedNumberTicker_Player2[3][1] && matchedNumberTicker_Player2[3][2]
            && matchedNumberTicker_Player2[3][3] && matchedNumberTicker_Player2[3][4])
        {
            player2Wins = true;
        }
        else if (matchedNumberTicker_Player2[4][0] && matchedNumberTicker_Player2[4][1] && matchedNumberTicker_Player2[4][2]
            && matchedNumberTicker_Player2[4][3] && matchedNumberTicker_Player2[4][4])
        {
            player2Wins = true;
        }
        //Vertical columns
        else if (matchedNumberTicker_Player2[0][0] && matchedNumberTicker_Player2[1][0] && matchedNumberTicker_Player2[2][0]
            && matchedNumberTicker_Player2[3][0] && matchedNumberTicker_Player2[4][0])
        {
            player2Wins = true;
        }
        else if (matchedNumberTicker_Player2[0][1] && matchedNumberTicker_Player2[1][1] && matchedNumberTicker_Player2[2][1]
            && matchedNumberTicker_Player2[3][1] && matchedNumberTicker_Player2[4][1])
        {
            player2Wins = true;
        }
        else if (matchedNumberTicker_Player2[0][2] && matchedNumberTicker_Player2[1][2] && matchedNumberTicker_Player2[2][2]
            && matchedNumberTicker_Player2[3][2] && matchedNumberTicker_Player2[4][2])
        {
            player2Wins = true;
        }
        else if (matchedNumberTicker_Player2[0][3] && matchedNumberTicker_Player2[1][3] && matchedNumberTicker_Player2[2][3]
            && matchedNumberTicker_Player2[3][3] && matchedNumberTicker_Player2[4][3])
        {
            player2Wins = true;
        }
        else if (matchedNumberTicker_Player2[0][4] && matchedNumberTicker_Player2[1][4] && matchedNumberTicker_Player2[2][4]
            && matchedNumberTicker_Player2[3][4] && matchedNumberTicker_Player2[4][4])
        {
            player2Wins = true;
        }
        //Diagonal
        else if (matchedNumberTicker_Player2[0][0] && matchedNumberTicker_Player2[1][1] && matchedNumberTicker_Player2[2][2]
            && matchedNumberTicker_Player2[3][3] && matchedNumberTicker_Player2[4][4])
        {
            player2Wins = true;
        }
        else if (matchedNumberTicker_Player2[0][4] && matchedNumberTicker_Player2[1][3] && matchedNumberTicker_Player2[2][2]
            && matchedNumberTicker_Player2[3][1] && matchedNumberTicker_Player2[4][0])
        {
            player2Wins = true;
        }
        //Four corners
        else if (matchedNumberTicker_Player2[0][0] && matchedNumberTicker_Player2[0][4]
            && matchedNumberTicker_Player2[4][0] && matchedNumberTicker_Player2[4][4])
        {
            player2Wins = true;
        }
        counter_CheckForPlayer2Matches++;
    }
       
    private static void checkIfGameHasEnded ()
    {
        //Check if either Player 1 or Player 2 has won - or if there is a tie
        
        if (player1Wins && !player2Wins)
        {
            headingText.setText("Player 1 Wins!");
            newNumberButton.setDisable(true);
        }
        else if (player2Wins && !player1Wins)
        {
            headingText.setText("Player 2 Wins!");
            newNumberButton.setDisable(true);
        }
        else if (player1Wins && player2Wins)
        {
            headingText.setText("Tie!");
            newNumberButton.setDisable(true);
        }
    }
    
    private static void clearAndResetGame ()
    {      
        //Invoked when the Reset button is clicked.
        //Reset everything and call the Generate and SetPosition methods
        
        for (int n = 0; n < bingoCardNumbers_Player1_Text.length; n++)
        {
            pane.getChildren().remove(bingoCardNumbers_Player1_Text[n]);
            pane.getChildren().remove(bingoCardNumbers_Player2_Text[n]);
        }
        
        for (int n = 0; n < drawnBingoNumbers_Text.length; n++)
        {
            pane.getChildren().remove(drawnBingoNumbers_Text[n]);
            drawnBingoNumbers_Text[n].setText(null);
        }
        
        for (int n = 0; n < line1_Player1.length; n++)
        {
            pane.getChildren().remove(line1_Player1[n]);
            pane.getChildren().remove(line2_Player1[n]);
        }
        
        for (int n = 0; n < line1_Player2.length; n++)
        {
            pane.getChildren().remove(line1_Player2[n]);
            pane.getChildren().remove(line2_Player2[n]);
        }
        counter = 0;
        yValueIncrementor = 0;
        
        generateRandomInts_Player1BingoCard();
        generateRandomInts_Player2BingoCard();
        
        setRandomIntsPosition_Player1BingoCard();
        setRandomIntsPosition_Player2BingoCard();
                
        generateBingoNumbersToBeDrawn();
        
        for (int i = 0; i < matchedNumberTicker_Player1.length; i++)
        {
            for (int j = 0; j < matchedNumberTicker_Player1[i].length; j++)
            {
                matchedNumberTicker_Player1[i][j] = false;
            }
        }
        counter_CheckForPlayer1Matches = 0;
        
        for (int i = 0; i < matchedNumberTicker_Player2.length; i++)
        {
            for (int j = 0; j < matchedNumberTicker_Player2[i].length; j++)
            {
                matchedNumberTicker_Player2[i][j] = false;
            }
        }
        counter_CheckForPlayer2Matches = 0;
        
        initializeFREESpots();
        
        player1Wins = false;
        player2Wins = false;
    }
    
    class PlayButtonHandler implements EventHandler<ActionEvent>
    {
        //Inner class for the Play button
        
        @Override
        public void handle(ActionEvent event) 
        {
            headingText.setText("Game In Progress...");
            
            newNumberButton.setDisable(false);
            resetButton.setDisable(false);
            
            drawAndDisplayNewNumber();
            
            checkForMatchedNumberAndWinner_Player1();
            checkForMatchedNumberAndWinner_Player2();
            
            playButton.setDisable(true);          
        }
    }
    
    class DrawNewNumberHandler implements EventHandler<ActionEvent>
    {
        //Inner class for the Draw New Number button
        
        @Override
        public void handle(ActionEvent event)
        {
            if (counter == 47)
            {
                newNumberButton.setDisable(true);
                if (!player1Wins && !player2Wins)
                {
                    headingText.setText("No winner! Please reset the game.");
                }
            }
            drawAndDisplayNewNumber();
            
            checkForMatchedNumberAndWinner_Player1();
            checkForMatchedNumberAndWinner_Player2();
            
            checkIfGameHasEnded();
        }
    }
    
    class ResetButtonHandler implements EventHandler<ActionEvent>
    {
        //Inner class for the Reset button
        
        @Override
        public void handle (ActionEvent event)
        {
            headingText.setText("Welcome To Bingo!");
                     
            resetButton.setDisable(true);
            newNumberButton.setDisable(true);
            playButton.setDisable(false);
            
            clearAndResetGame();
        }
    }
}

