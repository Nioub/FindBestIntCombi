package jo.zerezo.com.FindBestIntCombi;

/**
 * Find the combination of integer values whose sum equals to the goal value,
 * maximizing the number of values used
 * 
 * For example, if you have 1 2 3 and want to reach 3, then it will return 1 + 2 instead of 3 alone
 * 
 * @author Joël Thieffry jo@zerezo.com
 */
public class Finder {
	
	/** Value to find */
	public final int valueToFind;
	/** list of values to add to reach {@link #valueToFind} */
	public final int values[];
	/** Current best combination */
	private Long bestIComb = null;
	
	/**
	 * Constructor
	 * 
	 * @param valueToReach value to reach
	 * @param values list of values to add to reach {@code valueToReach}
	 */
	public Finder(final int valueToReach, final int values[]) {
		if (values == null || values.length == 0) { throw new IllegalArgumentException("values mustn't be null or empty"); }
		if (values.length > BitPermutationIterator.MAX_SIZE_IN_BITS) {
			throw new IllegalArgumentException("values can't have more that " + BitPermutationIterator.MAX_SIZE_IN_BITS + " elements");
		}
		
		this.valueToFind = valueToReach;
		this.values = values;
	}
	
	/**
	 * Size of the search space.
	 * 
	 * @return the size
	 */
	public long getNumberOfCombinations() {
		final long nbCombinaisons = Math.round(Math.pow(2, values.length));
		return nbCombinaisons;
	}
	
	/**
	 * Search from the beginning.
	 * 
	 * Resuming is not implemented.
	 * Progression callback not implemented.
	 * 
	 * @return {@code true} if found, {@code false} otherwise
	 */
	public boolean find() {
		// Iterate from all population counts, starting from the maximum
		for (int iBitCount = values.length; iBitCount >= 0; iBitCount--) {
			final LongIterator longIt = new BitPermutationIterator(values.length, iBitCount);
			long previousNum = 0L;
			int currentTotal = 0;
			while (longIt.hasNext()) {
				final long currentNum = longIt.nextLong();
				
				// Thanks to the low number of switched bits from previousNum à currentNum, and to the
				// associativity of addition, we can make binary diffs and adjust the sum accordingly
				final long xored = previousNum ^ currentNum;
				long removed = previousNum & xored;
				long added = currentNum & xored;
				long powerbit;
				while (0 != (powerbit = Long.lowestOneBit(removed))) {
					int bitNum = Long.numberOfTrailingZeros(powerbit);
					currentTotal -= values[bitNum];
					removed ^= powerbit;
				}
				while (0 != (powerbit = Long.lowestOneBit(added))) {
					int bitNum = Long.numberOfTrailingZeros(powerbit);
					currentTotal += values[bitNum];
					added ^= powerbit;
				}
				
				if (currentTotal == valueToFind) {
					// We found it!
					bestIComb = currentNum;
					return true;
				}
				// Otherwise go check next in line
				previousNum = currentNum;
			}
		}
		return false;
	}
	
	/**
	 * Return the best combination, if found, represented as a binary comb
	 * 
	 * @return the best combination, or {@code null} if none
	 */
	public Long getBestICombination() {
		return bestIComb;
	}
	
	/**
	 * Return the indexes used for the best combination, or an empty array if none
	 * 
	 * @return the indexes
	 */
	public int[] getBestIndexes() {
		if (bestIComb == null) { return new int[0]; }
		
		final int population = Long.bitCount(bestIComb);
		final int indexes[] = new int[population];
		int iPop = 0;
		long powerbit = 1L;
		for (int iBit = 0; iBit < values.length; iBit++) {
			final boolean utiliserBit = (bestIComb & powerbit) != 0;
			if (utiliserBit) {
				indexes[iPop] = iBit;
				iPop++;
			}
			powerbit += powerbit;
		}
		return indexes;
	}
	
	/**
	 * Build a simple representation of the values used for the best combination
	 * 
	 * @return a simple string
	 */
	public String displayBestValues() {
		if (bestIComb == null) { return ""; }
		
		final StringBuilder buf = new StringBuilder();
		boolean isFirst = true;
		for (final int index : getBestIndexes()) {
			if (isFirst) {
				isFirst = false;
			} else {
				buf.append(' ');
			}
			buf.append(values[index]);
		}
		return buf.toString();
	}
	
}
