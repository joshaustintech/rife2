/*
 * Copyright 2001-2022 Geert Bevin <gbevin[remove] at uwyn dot com>
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.datastructures;

public record DocumentPosition(String lineContent, int line, int column, int marking) {
}
