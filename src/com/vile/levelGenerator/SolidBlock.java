package com.vile.levelGenerator;

/**
 * Title: SolidBlock
 * @author Alex Byrd
 * Date Updated: 7/26/2016
 * 
 * Description:
 * Creates a SolidBlock, which is just a block that's solid. simple as
 * that.
 *
 */
public class SolidBlock extends Block 
{
   /**
    * Constructs the block, making sure it's solid.
    * @param h
    * @param wallID
    */
	public SolidBlock(int h, int wallID, double y, int x, int z)
	{
		super(h, wallID, y, x, z);
		isSolid = true;
	}
}
