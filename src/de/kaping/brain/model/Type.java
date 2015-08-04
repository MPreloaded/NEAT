package de.kaping.brain.model;

/**
 * Eine Enumeration, für die verschiedenen Typen von Neuronen.
 * Die aktuelle Sortierung ist wichtig für einen compareTo() Aufruf
 * @author MPreloaded
 */
public enum Type
{
	UNDEFINED,
	BIAS,
	INPUT,
	HIDDEN,
	OUTPUT;
}
