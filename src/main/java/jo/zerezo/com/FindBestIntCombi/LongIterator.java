package jo.zerezo.com.FindBestIntCombi;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A {@code LongIterator} is a specialized {@link Iterator} for {@link Long} elements
 * with a new method {@link #nextLong()} for faster access.
 * 
 * @author Joël Thieffry jo@zerezo.com
 */
public interface LongIterator extends Iterator<Long> {
	
	/**
	 * Returns the next element in iteration
	 * 
	 * @return the next element
	 * @throws NoSuchElementException if there is no more elements to iterate on
	 */
	long nextLong() throws NoSuchElementException;
	
}
