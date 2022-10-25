/*
 * #%L
 * KymographBuilder: Yet Another Kymograph Fiji plugin.
 * %%
 * Copyright (C) 2016 - 2022 Fiji developers.
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

package sc.fiji.kymographBuilder;

/**
 * Simple Line class. I called it Segment instead of Line to avoid name
 * conflict.
 *
 * @author Hadrien Mary
 */
class Segment {

	public final int xStart;
	public final int yStart;
	public final int xEnd;
	public final int yEnd;

	private Double length;

	public Segment(int xStart, int yStart, int xEnd, int yEnd) {
		this.xStart = xStart;
		this.yStart = yStart;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
	}

	public double getLength() {
		if (this.length == null) {
			this.length = Math.sqrt(Math.pow(this.xStart - this.xEnd, 2) + Math.pow(this.yStart -
				this.yEnd, 2));
		}
		return this.length;
	}

	public double[] getScaledVector() {
		double[] v = new double[2];
		double d = this.getLength();
		v[0] = (this.xStart - this.xEnd) / d;
		v[1] = (this.yStart - this.yEnd) / d;
		return v;
	}

	@Override
	public String toString() {
		String s = new String();
		s += "xStart : " + this.xStart + " | ";
		s += "yStart : " + this.yStart + " | ";
		s += "xEnd : " + this.xEnd + " | ";
		s += "yEnd: " + this.yEnd;
		return s;
	}

}
