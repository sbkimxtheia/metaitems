package io.github.sbkimxtheia.metaitems;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Optional;

public class AttrModification {
	final Attribute targetAttr;
	final AttributeModifier.Operation operation;
	final double value;
	final Optional<EquipmentSlot> slot;
	
	public AttrModification (Attribute targetAttr, AttributeModifier.Operation operation, double value, EquipmentSlot slot) {
		this.targetAttr = targetAttr;
		this.operation = operation;
		this.value = value;
		if(slot == null){
			this.slot = Optional.empty();
		}
		else{
			this.slot = Optional.of(slot);
		}
	}
	public AttrModification (Attribute targetAttr, AttributeModifier.Operation operation, double value){
		this(targetAttr,operation,value,null);
	}
}
