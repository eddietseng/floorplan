// =========================================================================
// RTree implementation.
// Copyright (C) 2002-2004 Wolfgang Baer - WBaer@gmx.de
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// =========================================================================

package model.rtree;

/**
 * <p>
 * DataObjects as stored in the RTree - a combination of Integer and the
 * corresponding HyperBoundingBox.
 * </p>
 * @author Wolfgang Baer - WBaer@gmx.de
 */

public class Data {

	private HyperBoundingBox box;
	private Integer intData;

	/**
	 * Constructor for Data.
	 * @param Integer the id of indexed object
	 * @param HyperBoundingBox the box.
	 */
	public Data(Integer intData, HyperBoundingBox box) {
		super();
		this.box = box;
		this.intData = intData;
	}
	
	/**
	 * Returns the Integer object.
	 * @return Integer object
	 */
	public Integer getInteger() {
		return intData;
	}
	
	/**
	 * Returns the value of the Integer object.
	 * @return int value.
	 */
	public int getIntegerValue() {
		return intData.intValue();
	}
	
	/**
	 * Returns the HyperBoundingBox.
	 * @return HyperBoundingBox box.
	 */
	public HyperBoundingBox getHyperBoundingBox() {
		return box;
	}

}
