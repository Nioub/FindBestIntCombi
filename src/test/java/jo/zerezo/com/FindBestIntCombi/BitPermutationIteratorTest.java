package jo.zerezo.com.FindBestIntCombi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class BitPermutationIteratorTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void whenSizeNotPositive() {
		new BitPermutationIterator(0, 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void whenSizeTooLarge() {
		new BitPermutationIterator(64, 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void whenBitcountNotPositiveOrNul() {
		new BitPermutationIterator(42, -1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void whenBitcountTooLarge() {
		new BitPermutationIterator(42, 43);
	}
	
	@Test
	public void whenEmpty() {
		final LongIterator it = new BitPermutationIterator(42, 0);
		final long next = it.nextLong();
		assertEquals(0L, next);
		assertFalse(it.hasNext());
	}
	
	@Test
	public void whenFull() {
		final LongIterator it = new BitPermutationIterator(4, 4);
		final long next = it.nextLong();
		assertEquals(15L, next);
		assertFalse(it.hasNext());
	}
	
	@Test
	public void size4() {
		final List<Long> expected = Arrays.asList(
				0L,
				1L, 2L, 4L, 8L,
				3L, 5L, 6L, 9L, 10L, 12L,
				7L, 11L, 13L, 14L,
				15L);
		final List<Long> computed = new ArrayList<>();
		dump(computed, 4);
		assertEquals(expected, computed);
	}
	
	@Test
	public void size5() {
		final List<Long> expected = Arrays.asList(
				0L,
				1L, 2L, 4L, 8L, 16L,
				3L, 5L, 6L, 9L, 10L, 12L, 17L, 18L, 20L, 24L,
				7L, 11L, 13L, 14L, 19L, 21L, 22L, 25L, 26L, 28L,
				15L, 23L, 27L, 29L, 30L,
				31L);
		final List<Long> computed = new ArrayList<>();
		dump(computed, 5);
		assertEquals(expected, computed);
	}
	
	private static void dump(final List<Long> into, final int sizeInBits, final int bitCount) {
		final LongIterator it = new BitPermutationIterator(sizeInBits, bitCount);
		while (it.hasNext()) {
			final long nextNum = it.next();
			into.add(nextNum);
		}
	}
	
	private static void dump(final List<Long> into, final int sizeInBits) {
		for (int iBitCount = 0; iBitCount <= sizeInBits; ++iBitCount) {
			dump(into, sizeInBits, iBitCount);
		}
	}
	
	public static String dispBinary(final long number, final int nbBits) {
		final String bin = Long.toBinaryString(number);
		final int binSize = bin.length();
		if (binSize >= nbBits) {
			return bin + " (" + number + ")";
		} else {
			return String.format("%0" + (nbBits - binSize) + "d", 0) + bin + " (" + number + ")";
		}
	}
	
}
