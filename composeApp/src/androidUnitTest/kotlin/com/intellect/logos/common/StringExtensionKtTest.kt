package com.intellect.logos.common

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class StringExtensionKtTest : BehaviorSpec({

    given("a Double number") {

        `when`("format is called with default parameters") {
            then("it should format the number with 2 decimal places and grouping") {
                val number = 1234567.89123
                number.format() shouldBe "1,234,567.89"
            }
        }

        `when`("format is called with minFraction = 1, maxFraction = 3, isGroupingUsed = false") {
            then("it should format the number accordingly") {
                val number = 1234567.89123
                number.format(
                    minFraction = 1,
                    maxFraction = 3,
                    isGroupingUsed = false
                ) shouldBe "1234567.891"
            }
        }

        `when`("format is called with minFraction = 0, maxFraction = 0") {
            then("it should round the number to the nearest integer") {
                val number = 1234567.89123
                number.format(minFraction = 0, maxFraction = 0) shouldBe "1,234,568"
            }
        }

        `when`("format is called with minFraction = 2, maxFraction = 5") {
            then("it should format the number with up to 5 decimal places") {
                val number = 1234567.89123
                number.format(minFraction = 2, maxFraction = 5) shouldBe "1,234,567.89123"
            }
        }

        `when`("format is called on a negative number") {
            then("it should format the negative number correctly") {
                val number = -1234.567
                number.format() shouldBe "-1,234.57"
            }
        }

        `when`("format is called with isGroupingUsed = false") {
            then("it should format the number without grouping") {
                val number = 1234567.89
                number.format(isGroupingUsed = false) shouldBe "1234567.89"
            }
        }

        `when`("format is called with minFraction greater than maxFraction") {
            then("it should throw an IllegalArgumentException") {
                val number = 1234.56
                shouldThrow<IllegalArgumentException> {
                    number.format(minFraction = 3, maxFraction = 2)
                }
            }
        }

        `when`("format is called with negative minFraction or maxFraction") {
            then("it should throw an IllegalArgumentException for minFraction") {
                val number = 1234.56
                shouldThrow<IllegalArgumentException> {
                    number.format(minFraction = -1)
                }
            }
            then("it should throw an IllegalArgumentException for maxFraction") {
                val number = 1234.56
                shouldThrow<IllegalArgumentException> {
                    number.format(maxFraction = -2)
                }
            }
        }

        `when`("format is called on zero") {
            then("it should return '0.00' with default parameters") {
                val number = 0.0
                number.format() shouldBe "0.00"
            }
            then("it should respect minFraction and maxFraction") {
                val number = 0.0
                number.format(minFraction = 1, maxFraction = 3) shouldBe "0.0"
            }
        }

        `when`("format is called on a number with fewer decimal places than minFraction") {
            then("it should pad the decimal part with zeros") {
                val number = 1234.5
                number.format(minFraction = 2, maxFraction = 4) shouldBe "1,234.50"
            }
        }

        `when`("format is called on a number with more decimal places than maxFraction") {
            then("it should round the number to maxFraction decimal places") {
                val number = 1234.56789
                number.format(minFraction = 0, maxFraction = 2) shouldBe "1,234.57"
            }
        }

    }
})