package edu.westminstercollege.cmpt355.minijava;

import java.util.Optional;

public sealed interface Type
    permits PrimitiveType, ClassType, VoidType {
}
