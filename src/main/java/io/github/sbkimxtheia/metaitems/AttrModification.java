package io.github.sbkimxtheia.metaitems;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Optional;

public class AttrModification {
	Attribute targetAttr;
	AttributeModifier.Operation operation;
	double value;
	Optional<EquipmentSlot> slot;
	
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
}
