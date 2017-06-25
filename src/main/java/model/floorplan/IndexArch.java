// ===========================================================
// Index mechanism for "architecture" viewpoint ....
// ===========================================================

package model.floorplan;

public class IndexArch {
   String siName;                 // Space index name ...
   Integer index;
   Object      o;
   private IndexArch typeOfIndex; // Stores type of space ...
       
   // Enumeration constants for "architecture" spatial index ...

   public static final IndexArch BLOCK  = new IndexArch("BLOCK");
   public static final IndexArch SPACE  = new IndexArch("SPACE");
   public static final IndexArch COLUMN = new IndexArch("COLUMN");
   public static final IndexArch CORNER = new IndexArch("CORNER");
   public static final IndexArch WALL   = new IndexArch("WALL");
   public static final IndexArch PORTAL   = new IndexArch("PORTAL");
   public static final IndexArch DOOR   = new IndexArch("DOOR");
   public static final IndexArch WINDOW   = new IndexArch("WINDOW");

   // Constructor methods ....

   public IndexArch ( String siName ) {
      this.siName = siName;
   }

   public IndexArch ( Integer index, Object o, final IndexArch typeOfSpace ) {
      this.index = index;
      this.o     =     o;
      this.setIndex ( typeOfSpace );
   }

   // Set/get "type" of spatial index ...

   public void setIndex ( final IndexArch typeOfIndex ) {
      this.typeOfIndex = typeOfIndex;
   }

   public IndexArch getIndex() { return this.typeOfIndex; }
   public    Object getSpace() { return this.o; }
}

