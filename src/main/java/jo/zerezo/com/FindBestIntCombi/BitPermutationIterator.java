package jo.zerezo.com.FindBestIntCombi;

import java.util.NoSuchElementException;

/**
 * Iterator that reviews all possible bit permutations
 * for a fixed number of bits set.
 * 
 * @author Joël Thieffry jo@zerezo.com
 */
public final class BitPermutationIterator implements LongIterator {

	/*
	 * Exemple with 4 places and population 4 :
	 * 1111  15   00
	 * 
	 * Exemple with 4 places and population 3 :
	 * 1110  14   01
	 * 1101  13   02
	 * 1011  11   04
	 * 0111  07   08
	 * 
	 * Exemple with 4 places and population 2 :
	 * 1100  12   03
	 * 1010  10   05
	 * 0110  06   09
	 * 1001  09   06
	 * 0101  05   10
	 * 0011  03   12
	 * 
	 * Exemple with 4 places and population 1 :
	 * 1000  08   07
	 * 0100  04   11
	 * 0010  02   13
	 * 0001  01   14
	 * 
	 * Exemple with 4 places and population 0 :
	 * 0000 (00)  15
	 * 
	 * 
	 * 00
	 *  01 02 04    08
	 *      03 05 06 09 10 12
	 *             07    11 13 14 15
	 */
	
	/*
	 * Exemple with 5 places and population 0 :
	 * 00000  00
	 * 
	 * Exemple with 5 places and population 1 :
	 * 00001  01
	 * 00010  02
	 * 00100  04
	 * 01000  08
	 * 10000  16
	 * 
	 * Exemple with 5 places and population 2 :
	 * 00011  03
	 * 00101  05
	 * 01001  09
	 * 00110  06
	 * 01010  10
	 * 01100  12
	 * 10001  17
	 * 10010  18
	 * 10100  20
	 * 11000  24
	 * 
	 * Exemple with 5 places and population 3 :
	 * 00111  07
	 * 01011  11
	 * 10011  19
	 * 01101  13
	 * 01110  14
	 * 10101  21
	 * 10110  22
	 * 11001  25
	 * 11010  26
	 * 11100  28
	 * 
	 * Exemple with 5 places and population 4 :
	 * 01111  15
	 * 10111  23
	 * 11011  27
	 * 11101  29
	 * 11110  30
	 * 
	 * Exemple with 5 places and population 5 :
	 * 11111  31
	 * 
	 * 
	 *      0   1       3               7                              15
	 *   0: O | . | .   . | .   .   .   . | .   .   .   .   .   .   .   . |     00
	 *   1:   | O | O   . | O   .   .   . | O   .   .   .   .   .   .   . |     01  1 2 4
	 *   2:   |   |     O |     O   O   . X     O   O   .   O   .   .   . |     03  2 1 3 1 2
	 *   3:   |   |       |             O |             O       O   O   . |     07      4 2 1
	 *   4:   |   |       |               |                             O |     15
	 * 
	 *      0   1       3               7                              15                                                              31  
	 *   0: O | . | .   . | .   .   .   . | .   .   .   .   .   .   .   . | .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   . |     00
	 *   1:   | O | O   . | O   .   .   . | O   .   .   .   .   .   .   . | O   .   .   .   .   .   .   .   .   .   .   .   .   .   .   . |     01  1 2 4 8
	 *   2:   |   |     O |     O   O   . |     O   O   .   O   .   .   . V     O   O   .   O   .   .   .   O   .   .   .   .   .   .   . |     03  2 1 3 1 2 5 1 2 4
	 *   3:   |   |       |             O |             O       O   O   . ^             O       O   O   .       O   O   .   O   .   .   . |     07  4 2 1 5 2 1 3 1 2
	 *   4:   |   |       |               |                             O |                             O               O       O   O   . |     15            8 4 2 1
	 *   5:   |   |       |               |                               |                                                             O |     31
	 */
	
	/** Maximum number of places, currently limited by the use of the {@link Long} type for counting */
	public static final int MAX_SIZE_IN_BITS = Long.SIZE - 1;
	/** Number of places, between 1 and {@link #MAX_SIZE_IN_BITS} */
	public final int sizeInBits;
	/** Population (number of bits to set), between 0 and {@link #sizeInBits} */
	public final int bitCount;
	
	private long nextNum;
	private long lastNum;
	private boolean isExhausted = false;
	
	/**
	 * Constructor
	 * 
	 * @param sizeInBits number of places, between 1 and {@link #MAX_SIZE_IN_BITS}
	 * @param bitCount Population (number of bits to set), between 0 and {@code sizeInBits}
	 * @throws IllegalArgumentException if preconditions not respected for parameters
	 */
	public BitPermutationIterator(final int sizeInBits, final int bitCount) {
		if (sizeInBits <= 0) { throw new IllegalArgumentException("sizeInBits must be positive"); }
		if (sizeInBits > MAX_SIZE_IN_BITS) { throw new IllegalArgumentException("sizeInBits must be at most " + MAX_SIZE_IN_BITS); }
		if (bitCount < 0) { throw new IllegalArgumentException("bitCount must be positive or nul"); }
		if (bitCount > sizeInBits) { throw new IllegalArgumentException("bitCount must be at most sizeInBits"); }
		
		this.sizeInBits = sizeInBits;
		this.bitCount = bitCount;
		
		if (bitCount == 0) {
			// Case when there is no population
			nextNum = 0L;
			lastNum = 0L;
		} else if (bitCount == sizeInBits) {
			// Case when the population fill all the places
			if (sizeInBits == MAX_SIZE_IN_BITS) {
				nextNum = Long.MAX_VALUE;
			} else {
				nextNum = (1L<<sizeInBits) - 1L;
			}
			lastNum = nextNum;
		} else {
			// Nominal case
			nextNum = (1L<<bitCount) - 1L;
			lastNum = ((1L<<sizeInBits) - 1) ^ (((1L<<(sizeInBits - bitCount)) - 1));
		}
	}
	
	/** {@inheritDocs} */
	@Override
	public boolean hasNext() {
		return !isExhausted;
	}

	/** {@inheritDocs} */
	@Override
	public Long next() {
		if (!hasNext()) {
			return null;
		}
		return nextLong();
	}
	
	/** {@inheritDocs} */
	@Override
	public long nextLong() throws NoSuchElementException {
		if (isExhausted) { throw new NoSuchElementException(); }
		
		final long currentNum = nextNum;
		if (nextNum == lastNum) {
			isExhausted = true;
		} else {
			// Bit permutation algorithm
			// @see http://graphics.stanford.edu/~seander/bithacks.html#NextBitPermutation
			
			// t gets currentNum's least significant 0 bits set to 1
			final long t = currentNum | (currentNum - 1);
			
			// Next set to 1 the most significant bit to change,
			// set to 0 the least significant ones, and add the necessary 1 bits.
			nextNum = (t + 1L) | (((~t & -~t) - 1L) >> (Long.numberOfTrailingZeros(currentNum) + 1));
		}
		return currentNum;
	}
	
}
