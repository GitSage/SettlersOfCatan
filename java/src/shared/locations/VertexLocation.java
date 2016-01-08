package shared.locations;

/**
 * Represents the location of a vertex on a hex map
 */
public class VertexLocation
{
	
	private HexLocation hexLoc;
	private VertexDirection dir;
	
	public VertexLocation(HexLocation hexLoc, VertexDirection dir)
	{
		setHexLoc(hexLoc);
		setDir(dir);
	}
	
	public HexLocation getHexLoc()
	{
		return hexLoc;
	}
	
	private void setHexLoc(HexLocation hexLoc)
	{
		if(hexLoc == null)
		{
			throw new IllegalArgumentException("hexLoc cannot be null");
		}
		this.hexLoc = hexLoc;
	}
	
	public VertexDirection getDir()
	{
		return dir;
	}
	
	private void setDir(VertexDirection direction)
	{
		this.dir = direction;
	}
	
	@Override
	public String toString()
	{
		return "VertexLocation [hexLoc=" + hexLoc + ", dir=" + dir + "]";
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		result = prime * result + ((hexLoc == null) ? 0 : hexLoc.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		VertexLocation other = (VertexLocation)obj;
		if(dir != other.dir)
			return false;
		if(hexLoc == null)
		{
			if(other.hexLoc != null)
				return false;
		}
		else if(!hexLoc.equals(other.hexLoc))
			return false;
		return true;
	}

	/**
	 * Gets a vertex adjacent to this vertex.
	 * @pre this vertex location must be a normalized location, meaning
	 *      getDirection() returns NorthWest or NorthEast.
	 * @pre dir must be an allowed VertexDirection. Allowable directions depend on the value of this.getDirection().
	 *      If this.getDirection() returns NorthWest, dir may be East, NorthWest, or SouthWest.
	 *      If this.getDirection() returns NorthEast, dir may be West, NorthEast, or SouthEast.
	 * @pre this vertex location must not be on the upper
	 * @post returns the VertexLocation in the direction specified.
	 * @param dir VertexDirection
	 * @return VertexLocation
	 */
	public VertexLocation getAdjacentVertex(VertexDirection dir){
		if(getDir() == VertexDirection.NorthWest){
			switch(dir){
				case East:
					return new VertexLocation(getHexLoc(), VertexDirection.NorthEast);
				case NorthWest:
					return new VertexLocation(getHexLoc().getNeighborLoc(EdgeDirection.NorthWest), VertexDirection.NorthEast);
				case SouthWest:
					return new VertexLocation(getHexLoc().getNeighborLoc(EdgeDirection.SouthWest), VertexDirection.NorthEast);
				default: // dir is invalid
					return null;
			}
		}
		else if(getDir() == VertexDirection.NorthEast){
			switch(dir){
				case West:
					return new VertexLocation(getHexLoc(), VertexDirection.NorthWest);
				case NorthEast:
					return new VertexLocation(getHexLoc().getNeighborLoc(EdgeDirection.NorthEast), VertexDirection.NorthWest);
				case SouthEast:
					return new VertexLocation(getHexLoc().getNeighborLoc(EdgeDirection.SouthEast), VertexDirection.NorthWest);
				default: // dir is invalid
					return null;
			}
		}
		// dir is not normalized
		else{
			return null;
		}

	}
	
	/**
	 * Returns a canonical (i.e., unique) value for this vertex location. Since
	 * each vertex has three different locations on a map, this method converts
	 * a vertex location to a single canonical form. This is useful for using
	 * vertex locations as map keys.
	 * 
	 * @return Normalized vertex location
	 */
	public VertexLocation getNormalizedLocation()
	{
		
		// Return location that has direction NW or NE
		
		switch (dir)
		{
			case NorthWest:
			case NorthEast:
				return this;
			case West:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.SouthWest),
										  VertexDirection.NorthEast);
			case SouthWest:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.South),
										  VertexDirection.NorthWest);
			case SouthEast:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.South),
										  VertexDirection.NorthEast);
			case East:
				return new VertexLocation(
										  hexLoc.getNeighborLoc(EdgeDirection.SouthEast),
										  VertexDirection.NorthWest);
			default:
				assert false;
				return null;
		}
	}
}

