import java.util.Iterator;

public class SeamInfo implements Iterable<SeamInfo> {

  Pixel correspondant;
  double totalWeight; //so far
  SeamInfo cameFrom; //containing info on the seam up until this point
  
  public SeamInfo(Pixel correspondant, double totalWeight, SeamInfo cameFrom) {
    this.correspondant = correspondant;
    this.totalWeight = totalWeight;
    this.cameFrom = cameFrom;
  }

  @Override
  public Iterator<SeamInfo> iterator() {
    return new SeamIterator(this);
  }
  
  
  
}

class SeamIterator implements Iterator<SeamInfo> {

  SeamInfo seam;
  
  public SeamIterator(SeamInfo seam) {
    this.seam = seam;
  }
  
  @Override
  public boolean hasNext() {
    return seam.cameFrom != null;
  }

  @Override
  public SeamInfo next() {
    SeamInfo next = this.seam.cameFrom;
    seam = seam.cameFrom;
    return next;
  }
  
}
