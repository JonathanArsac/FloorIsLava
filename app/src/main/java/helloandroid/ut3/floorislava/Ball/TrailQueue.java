package helloandroid.ut3.floorislava.Ball;

import java.util.LinkedList;

class TrailQueue<E> extends LinkedList<E> {

   private int trailLimit;

   public TrailQueue(int trailLimit) {
      super();
      this.trailLimit = trailLimit;
   }

   @Override
   public boolean add(E e) {
      if (size() >= trailLimit) {
         removeFirst();
      }
      return super.add(e);
   }
}
