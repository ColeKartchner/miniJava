package edu.westminstercollege.cmpt355.minijava;

import java.util.Objects;

sealed class ClassType implements Type permits StaticType {

        public  String className;

        public ClassType(String className) {
                this.className = className;
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


        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                ClassType classType = (ClassType) o;
                return Objects.equals(className, classType.className);
        }

        @Override
        public int hashCode() {
                return Objects.hash(className);
        }
}
