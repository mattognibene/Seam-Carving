import java.util.ArrayList;

import javax.swing.plaf.synth.SynthSeparatorUI;

import tester.*;
import javalib.impworld.*;
import javalib.worldimages.*;
import java.awt.Color;

public class SeamCarver extends World {
  
  FromFileImage img;
  ArrayList<ArrayList<Pixel>> bitmap;
  
  public SeamCarver(String fileName) {
    this.img = new FromFileImage(fileName);
    this.bitmap = new ArrayList<ArrayList<Pixel>>();

    this.initializeBitmap();

  }

  // initializes all the pixels
  private void initializeBitmap() {
    for (int c = 0; c < this.img.getWidth(); c++) {
      this.bitmap.add(new ArrayList<Pixel>());
      for (int r = 0; r < this.img.getHeight(); r++) {
        this.bitmap.get(c).add(new Pixel(c, r, this.img.getColorAt(c, r)));
      }
    }
    
    this.initializeEdges();
    for (int c = 0; c < bitmap.size(); c++) {
      for (int r = 0; r < bitmap.get(0).size(); r++) {
        this.bitmap.get(c).get(r).calculateEnergies();
      }
    }
  }

  //connects the bitmap
  private void initializeEdges() {

    int width = bitmap.size();
    int height = bitmap.get(0).size();
    
    
    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height; row++)  {
        for (int c = -1; c < 2; c++) {
          for (int r = -1; r < 2; r++) {
            if (col + c < width && col + c >= 0
                && row + r < height && row + r >= 0) {
              if (!(c == 0 && r == 0)) {
                this.bitmap.get(col).get(row).addEdge(
                    new Edge(this.bitmap.get(col).get(row),
                        this.bitmap.get(col + c).get(row + r)));
              }
            }
            else {
              this.bitmap.get(col).get(row).addEdge(
                  new Edge(this.bitmap.get(col).get(row),
                      new Pixel(col + c, row + r, Color.BLACK)));
            }
          }
        }
      }
    }
  }

  

  @Override
  public WorldScene makeScene() {
    WorldScene base = new WorldScene((int) this.img.getWidth(), (int) this.img.getHeight());
    base.placeImageXY(img, (int) this.img.getWidth() / 2, (int) this.img.getHeight() / 2);
    //base.placeImageXY(drawSeam(), (int) this.img.getWidth() / 2, (int) this.img.getHeight() / 2);
    base.placeImageXY(drawEnergyMap(), (int) this.img.getWidth() / 2, (int) this.img.getHeight() / 2);
    return base;
  }
  
  WorldImage drawEnergyMap() {
    ComputedPixelImage base = new ComputedPixelImage(this.bitmap.size(), this.bitmap.get(0).size());
    ArrayList<ArrayList<SeamInfo>> seamMap = getSeamMap();
    double heighestWeight = seamMap.get(0).get(seamMap.get(0).size()-1).totalWeight;
    for (int c = 0; c < seamMap.size(); c++) {
      if(heighestWeight < seamMap.get(c).get(seamMap.get(0).size()-1).totalWeight) {
        heighestWeight = seamMap.get(c).get(seamMap.get(0).size()-1).totalWeight;
      }
    }

    for(ArrayList<SeamInfo> losi : seamMap) {
      for(SeamInfo s : losi) {
        base.setPixel(s.correspondant.col, s.correspondant.row,
            new Color((int) (255 * (s.totalWeight / heighestWeight))));
      }
    }
    
    return base;
  }
  
  WorldImage drawSeam() {
    SeamInfo seam = this.findSeamVertical();
    ComputedPixelImage base = new ComputedPixelImage(this.bitmap.size(), this.bitmap.get(0).size());
    for (int c = 0; c < this.bitmap.size(); c++) {
      for (int r = 0; r < this.bitmap.get(0).size(); r++) {
        base.setPixel(c, r, this.img.getColorAt(c, r));
      }
    }
    
    for(SeamInfo s: seam) {
      base.setPixel(s.correspondant.col, s.correspondant.row, Color.RED);
    }
    
    
    return base;
    
  }
  
  private SeamInfo findSeamVertical() {
    ArrayList<ArrayList<SeamInfo>> seamMap = getSeamMap();
    SeamInfo best = seamMap.get(0).get(seamMap.get(0).size()-1);
    for(int c = 0; c < seamMap.size(); c++) {
      if(seamMap.get(c).get(seamMap.get(0).size() -1).totalWeight < best.totalWeight) {
        best = seamMap.get(c).get(seamMap.get(0).size()-1);
      }
    }
    
    
    return best;    
  }

  //returns the seam map
  ArrayList<ArrayList<SeamInfo>> getSeamMap() {
    ArrayList<ArrayList<SeamInfo>> seamMap = new ArrayList<ArrayList<SeamInfo>>();
    for(int c = 0; c < this.bitmap.size(); c++) {
      seamMap.add(new ArrayList<SeamInfo>());
      seamMap.get(c).add(new SeamInfo(this.bitmap.get(c).get(0), this.bitmap.get(c).get(0).energy, null));
    }
    
    for (int r = 1; r < this.bitmap.get(0).size(); r++) {
      for(int c = 0; c < this.bitmap.size(); c++) {
        SeamInfo bestPrevious = seamMap.get(c).get(r-1);
        for(int cc = -1; cc < 2; cc++) {
          if(cc + c >= 0 && cc + c < seamMap.size() && seamMap.get(c + cc).get(r-1).totalWeight < bestPrevious.totalWeight) {
            bestPrevious = seamMap.get(c + cc).get(r - 1);
          }
        }
        seamMap.get(c).add(new SeamInfo(this.bitmap.get(c).get(r),
            bestPrevious.totalWeight + this.bitmap.get(c).get(r).energy, bestPrevious));
      }
    }
    
    return seamMap;
    
  }
  
  void removeSeam(SeamInfo seam) {
    for(SeamInfo s: seam) {
      s.correspondant.findPixel(-1, 0).removeEdge(1, 0);
      s.correspondant.findPixel(-1, 0).addEdge();
    }
  }
}
