import tester.Tester;

public class Examples {
  SeamCarver w;
  void initData() {
    w = new SeamCarver("img2.jpg");
  }
  
  void testGame(Tester t) {
    initData();
    w.bigBang((int) w.img.getWidth(), (int) w.img.getHeight());
  }
  
  void testInitializeBitMap(Tester t) {
    initData();
    //initializebitmap is called in the constructor
    t.checkExpect(w.bitmap.size() * 1.0, w.img.getWidth());
    t.checkExpect(w.bitmap.get(0).size() * 1.0, w.img.getHeight());
    for (int c = 0; c < 10; c++) {
      for (int r = 0; r < 10; r++) {
        double br = w.img.getColorAt(c, r).getBlue() + w.img.getColorAt(c, r).getRed() 
            + w.img.getColorAt(c, r).getGreen();
        br /= 3.0;
        br /= 255.0;
        t.checkInexact(w.bitmap.get(c).get(r).brightness, br, .01);
      }
    }
  }
  
 /* void testCalculateEnergies(Tester t) {
    initData();
    t.checkInexact(w.bitmap.get(0).get(0).energy, 0, .01);
  }*/
  
  
}

