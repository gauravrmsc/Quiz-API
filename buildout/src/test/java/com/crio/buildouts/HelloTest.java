
package com.crio.buildouts;

import com.volvain.buildout.BuildOutApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BuildOutApplication.class})
public class HelloTest {

  /**
   * This is where you can write your tests.
   */
  @Test
  public void test() {
    System.out.println("hello world");
    assert (true);
  }

}
