package org;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Topic {
   private int number;
   private String title;

   public Topic (int number, String title) {
       this.number = number;
       this.title = title;
   }

   @Override
    public String toString() {
        return "Number: "+ number +"\n"+ "Title: "+title+"\n";

   }
}
