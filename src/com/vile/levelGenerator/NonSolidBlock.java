package com.vile.levelGenerator;

/**
 * Title: NonSolidBlock
 * @author Alex Byrd
 * Date Updated: 7/26/2016
 * 
 * Description:
 * Creates a non solid block. At the moment all this does is make sure
 * that the block is not solid.
 *
 */
public class NonSolidBlock extends Block
{
   /**
    * Constructs the non solid block. The only quality this has is it is
    * not solid. In the future this may become more advanced, but right
    * now this isn't even really being used for effeciency purposes
    * @param h
    * @param wallID
    */
	public NonSolidBlock(int h, int wallID, double y, int x, int z)
	{
		super(h, wallID, y, x, z);
		isSolid = false;
	}
}
