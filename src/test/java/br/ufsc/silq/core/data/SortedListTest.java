package br.ufsc.silq.core.data;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SortedListTest {

	@Test
	public void testAdd() {
		SortedList<Integer> list = new SortedList<>();

		list.add(2);
		Assertions.assertThat(list).isEqualTo(Arrays.asList(2));

		list.add(1);
		Assertions.assertThat(list).isEqualTo(Arrays.asList(1, 2));

		list.add(1);
		Assertions.assertThat(list).isEqualTo(Arrays.asList(1, 1, 2));

		list.add(-4);
		Assertions.assertThat(list).isEqualTo(Arrays.asList(-4, 1, 1, 2));
	}

	@Test
	public void testAddAt() {
		// Índice deve ser ignorado
		SortedList<String> list = new SortedList<>();

		list.add(2, "d");
		Assertions.assertThat(list).isEqualTo(Arrays.asList("d"));

		list.add(3, "c");
		Assertions.assertThat(list).isEqualTo(Arrays.asList("c", "d"));

		list.add(0, "z");
		Assertions.assertThat(list).isEqualTo(Arrays.asList("c", "d", "z"));

		list.add(42, "d");
		Assertions.assertThat(list).isEqualTo(Arrays.asList("c", "d", "d", "z"));

		list.add(1, "a");
		Assertions.assertThat(list).isEqualTo(Arrays.asList("a", "c", "d", "d", "z"));
	}

	@Test
	public void testAddAll() {
		SortedList<Float> list = new SortedList<>();
		list.addAll(Arrays.asList(5f, 3.0f, 3f, 1.0f, -10.0f));

		Assertions.assertThat(list).isEqualTo(Arrays.asList(-10.0f, 1.0f, 3.0f, 3f, 5f));
	}

	@Test
	public void testCreate() {
		SortedList<Double> list = new SortedList<>(Arrays.asList(5.0, 1.0, -42.0, 1.0));
		Assertions.assertThat(list).isEqualTo(Arrays.asList(-42.0, 1.0, 1.0, 5.0));
	}
}
