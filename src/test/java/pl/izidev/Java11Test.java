package pl.izidev;

import static org.junit.Assert.assertEquals;
import java.util.stream.Stream;
import org.junit.Test;

public class Java11Test {

	@Test
	public void dummy() {
		assertEquals(
			Stream.of(
				new String[]{ "Testing", "Java11" }
			).count(),
			2L
		);
	}

}
