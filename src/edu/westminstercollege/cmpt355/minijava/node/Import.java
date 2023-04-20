package edu.westminstercollege.cmpt355.minijava.node;

public sealed interface Import extends Node
        permits ClassImport, PackageImport{
}
