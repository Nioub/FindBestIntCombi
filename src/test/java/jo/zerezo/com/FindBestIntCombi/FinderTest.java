package jo.zerezo.com.FindBestIntCombi;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FinderTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void whenNoValues() {
		new Finder(0, new int[0]);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void whenTooManyValues() {
		new Finder(0, new int[Long.SIZE]);
	}
	
	@Test
	public void nominalSimple() {
		final Finder finder = new Finder(1 + 4 + 8, new int[] {1, 2, 4, 8, 16});
		final long nbCombi = finder.getNumberOfCombinations();
		assertEquals(2*2*2*2*2, nbCombi);
		
		final boolean found = finder.find();
		assertTrue(found);
		
		final long best = finder.getBestICombination();
		assertEquals(0b1101L, (long) best);
		
		final int[] indexes = finder.getBestIndexes();
		assertArrayEquals(new int[] {0, 2, 3}, indexes);
		
		final String display = finder.displayBestValues();
		assertEquals("1 4 8", display);
	}
	
	@Test
	public void nominal123order() {
		final Finder finder = new Finder(1 + 2, new int[] {3, 199, 5000, 2, 1, 98798});
		final long nbCombi = finder.getNumberOfCombinations();
		assertEquals(2*2*2*2*2*2, nbCombi);
		
		final boolean found = finder.find();
		assertTrue(found);
		
		final long best = finder.getBestICombination();
		assertEquals(0b011000L, (long) best);
		
		final int[] indexes = finder.getBestIndexes();
		assertArrayEquals(new int[] {3, 4}, indexes);
		
		final String display = finder.displayBestValues();
		assertEquals("2 1", display);
	}
	
	@Test
	public void whenNotFound() {
		final Finder finder = new Finder(128, new int[] {1, 2, 4, 8, 16});
		final long nbCombi = finder.getNumberOfCombinations();
		assertEquals(2*2*2*2*2, nbCombi);
		
		final boolean found = finder.find();
		assertFalse(found);
		
		final Long best = finder.getBestICombination();
		assertNull(best);
		
		final int[] indexes = finder.getBestIndexes();
		assertArrayEquals(new int[0], indexes);
		
		final String display = finder.displayBestValues();
		assertEquals("", display);
	}
	
}
