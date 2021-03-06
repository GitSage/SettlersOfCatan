package shared.definitions;

import com.google.gson.annotations.SerializedName;

import java.awt.Color;

public enum CatanColor
{

	@SerializedName("red")
	RED,

	@SerializedName("orange")
	ORANGE,

	@SerializedName("yellow")
	YELLOW,

	@SerializedName("blue")
	BLUE,

	@SerializedName("green")
	GREEN,

	@SerializedName("purple")
	PURPLE,

	@SerializedName("puce")
	PUCE,

	@SerializedName("white")
	WHITE,

	@SerializedName("brown")
	BROWN;

	private Color color;
	
	static
	{
		RED.color = new Color(227, 66, 52);
		ORANGE.color = new Color(255, 165, 0);
		YELLOW.color = new Color(253, 224, 105);
		BLUE.color = new Color(111, 183, 246);
		GREEN.color = new Color(109, 192, 102);
		PURPLE.color = new Color(157, 140, 212);
		PUCE.color = new Color(204, 136, 153);
		WHITE.color = new Color(223, 223, 223);
		BROWN.color = new Color(161, 143, 112);
	}

	public Color getJavaColor()
	{
		return color;
	}
}

