import java.awt.Color;
import java.util.ArrayList;

public class Pixel {
  
  Color color;
  double brightness;
  double horizontalEnergy;
  double verticalEnergy;
  double energy;
  int row;
  int col;
  Pixel[][] neighbors = new Pixel[3][3]; //row major order (confusing i know)
  
  //EFFECT: initializes this pixels brightness, row and col
  //its neighbors is NOT initialized because this pixel does not have access to its surroundings!
  //neighbor initialization MUST occur externally
  public Pixel(int col, int row, Color color) {
    this.color = color;
    this.col = col;
    this.row = row;
    this.brightness = calculateBrightness(this.color);
  }
   
  
  private double calculateBrightness(Color colorAt) {
    double average = (colorAt.getGreen() + colorAt.getBlue() + colorAt.getRed()) / 3.0;
    return average / 255.0;
    
  }
  
  //calculates energy
  //BE SURE TO INITIALIZE NEIGHBORS BEFORE
  void calculateEnergies() {
    
    double leftColumn = this.neighbors[0][0].brightness + (2 * this.neighbors[1][0].brightness)
        + this.neighbors[2][0].brightness;
    double rightColumn = this.neighbors[0][2].brightness + (2 * this.neighbors[1][2].brightness)
        + this.neighbors[2][2].brightness;
    this.horizontalEnergy = leftColumn - rightColumn;
      
    double topRow = this.neighbors[0][0].brightness + (2 * this.neighbors[0][1].brightness)
        + this.neighbors[0][2].brightness;
    double bottomRow = this.neighbors[2][0].brightness + (2 * this.neighbors[2][1].brightness)
        + this.neighbors[2][2].brightness;
    this.verticalEnergy = topRow - bottomRow;
    
    this.energy = Math.sqrt(Math.pow(horizontalEnergy, 2) + Math.pow(verticalEnergy, 2));
  }
  
  
  
  //for testing purposes
  public String toString() {
    return "(" +  Integer.toString(col) + ", " + Integer.toString(row) + ")";
  }
  
}
