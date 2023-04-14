package edu.westminstercollege.cmpt355.minijava;

import java.util.Objects;

sealed class ClassType implements Type permits StaticType {

        public  String className;

        public ClassType(String className) {
        }


        public String ClassType(String className) {
                return this.className = className;
        }

        public String toString() {
                return java.lang.String.format("ClassType[%s]", className);
        }


        public String getClassName() {
                return className;
        }


        public int getHashCode() {
                return Objects.hash(className);
        }



}
