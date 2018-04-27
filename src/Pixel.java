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
  ArrayList<Edge> neighbors;
  
  //EFFECT: initializes this pixels brightness, row and col
  //its neighbors is NOT initialized because this pixel does not have access to its surroundings!
  //neighbor initialization MUST occur externally
  public Pixel(int col, int row, Color color) {
    this.color = color;
    this.col = col;
    this.row = row;
    this.brightness = calculateBrightness(this.color);
    this.neighbors = new ArrayList<Edge>();
  }
  
  void addEdge(Edge e) {
    neighbors.add(e);
  } 
  
  private double calculateBrightness(Color colorAt) {
    double average = (colorAt.getGreen() + colorAt.getBlue() + colorAt.getRed()) / 3.0;
    return average / 255.0;
    
  }
  
  //calculates energy
  //BE SURE TO INITIALIZE NEIGHBORS BEFORE
  void calculateEnergies() {
    if (this.neighbors.size() < 8) {
      //printing an error instead of throwing an exception
      System.out.println("WARNING: The bitmap has not been correctly connected!");
    }
    double leftColumn = this.findPixel(-1, -1).brightness + (2 * this.findPixel(-1, 0).brightness)
        + this.findPixel(-1, 1).brightness;
    double rightColumn = this.findPixel(1, -1).brightness + (2 * this.findPixel(1, 0).brightness)
        + this.findPixel(1, 1).brightness;
    this.horizontalEnergy = leftColumn - rightColumn;
      
    double topRow = this.findPixel(-1, -1).brightness + (2 * this.findPixel(0, -1).brightness)
        + this.findPixel(1, -1).brightness;
    double bottomRow = this.findPixel(-1, 1).brightness + (2 * this.findPixel(0, 1).brightness)
        + this.findPixel(1, 1).brightness;
    this.verticalEnergy = topRow - bottomRow;
    
    this.energy = Math.sqrt(Math.pow(horizontalEnergy, 2) + Math.pow(verticalEnergy, 2));
  }
  
  //finds the pixel that is dx and dy away from this pixel in neighbors
  Pixel findPixel(int dx, int dy) {
    for(Edge e : neighbors) {
      if (e.to.col == this.col + dx && e.to.row == this.row + dy) {
        return e.to;
      }
    }
    
    throw new RuntimeException("ERROR: Could not find pixel in neighbors");
  }
  
  //for testing purposes
  public String toString() {
    return "(" +  Integer.toString(col) + ", " + Integer.toString(row) + ")";
  }
  
}
