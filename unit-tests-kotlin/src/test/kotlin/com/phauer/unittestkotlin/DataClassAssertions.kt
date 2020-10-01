package com.phauer.unittestkotlin

import io.kotest.assertions.asClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.equality.shouldBeEqualToUsingFields
import io.kotest.matchers.shouldBe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DataClassAssertions {

    //Don't
    @Test
    fun test() {
        val client = DesignClient()

        val actualDesign = client.requestDesign(id = 1)

        assertThat(actualDesign.id).isEqualTo(2)
        assertThat(actualDesign.userId).isEqualTo(9)
        assertThat(actualDesign.name).isEqualTo("Cat")

        /*
        org.junit.ComparisonFailure: expected:<[2]> but was:<[1]>
        Expected :2
        Actual   :1
         */
    }

    @Test
    fun test_kotest() {
        val client = DesignClient()

        val actualDesign = client.requestDesign(id = 1)

        actualDesign.id shouldBe 2 // ComparisonFailure
        actualDesign.userId shouldBe 9
        actualDesign.name shouldBe "Cat"

        /*
        org.opentest4j.AssertionFailedError: expected:<2> but was:<1>
        Expected :2
        Actual   :1
         */
    }

    //Do
    @Test
    fun test2() {
        val client = DesignClient()

        val actualDesign = client.requestDesign(id = 1)

        val expectedDesign = Design(
            id = 2,
            userId = 9,
            name = "Cat"
        )
        assertThat(actualDesign).isEqualTo(expectedDesign)

        /*
        org.junit.ComparisonFailure: expected:<Design(id=[2], userId=9, name=Cat...> but was:<Design(id=[1], userId=9, name=Cat...>
        Expected :Design(id=2, userId=9, name=Cat)
        Actual   :Design(id=1, userId=9, name=Cat)
         */
    }

    @Test
    fun test2_kotest() {
        val client = DesignClient()

        val actualDesign = client.requestDesign(id = 1)

        val expectedDesign = Design(
            id = 2,
            userId = 9,
            name = "Cat"
        )
        actualDesign shouldBe expectedDesign

        /*
        org.opentest4j.AssertionFailedError: data class diff for de.philipphauer.blog.unittestkotlin.Design
        └ id: expected:<2> but was:<1>

        expected:<Design(id=2, userId=9, name=Cat)> but was:<Design(id=1, userId=9, name=Cat)>
        Expected :Design(id=2, userId=9, name=Cat)
        Actual   :Design(id=1, userId=9, name=Cat)
         */
    }

    //Do
    @Test
    fun lists() {
        val client = DesignClient()

        val actualDesigns = client.getAllDesigns()

        assertThat(actualDesigns).containsExactly(
            Design(
                id = 1,
                userId = 9,
                name = "Cat"
            ),
            Design(
                id = 2,
                userId = 4,
                name = "Dog"
            )
        )
        /*
        java.lang.AssertionError:
        Expecting:
          <[Design(id=1, userId=9, name=Cat),
            Design(id=2, userId=4, name=Dogggg)]>
        to contain exactly (and in same order):
          <[Design(id=1, userId=9, name=Cat),
            Design(id=2, userId=4, name=Dog)]>
        but some elements were not found:
          <[Design(id=2, userId=4, name=Dog)]>
        and others were not expected:
          <[Design(id=2, userId=4, name=Dogggg)]>
         */
    }

    @Test
    fun lists_kotest() {
        val client = DesignClient()

        val actualDesigns = client.getAllDesigns()

        actualDesigns.shouldContainExactly(
            Design(
                id = 1,
                userId = 9,
                name = "Cat"
            ),
            Design(
                id = 2,
                userId = 4,
                name = "Dog"
            )
        )
        /*
        java.lang.AssertionError: Expecting: [
          Design(id=1, userId=9, name=Cat),
          Design(id=2, userId=4, name=Dog)
        ] but was: [
          Design(id=1, userId=9, name=Cat),
          Design(id=2, userId=4, name=Dogggg)
        ]
        Some elements were missing: [
          Design(id=2, userId=4, name=Dog)
        ] and some elements were unexpected: [
          Design(id=2, userId=4, name=Dogggg)
        ]
         */
    }

    @Test
    fun sophisticatedAssertions_single() {
        val client = DesignClient()

        val actualDesign = client.requestDesign(id = 1)

        val expectedDesign = Design(
            id = 2,
            userId = 9,
            name = "Cat"
        )
        assertThat(actualDesign).isEqualToIgnoringGivenFields(expectedDesign, "id")
        assertThat(actualDesign).isEqualToComparingOnlyGivenFields(expectedDesign, "userId", "name")
    }

    @Test
    fun sophisticatedAssertions_single_kotest() {
        val client = DesignClient()

        val actualDesign = client.requestDesign(id = 1)

        val expectedDesign = Design(
            id = 2,
            userId = 9,
            name = "Cat"
        )
        actualDesign.shouldBeEqualToIgnoringFields(expectedDesign, Design::id)
        actualDesign.shouldBeEqualToUsingFields(expectedDesign, Design::userId, Design::name)
    }

    @Test
    fun sophisticatedAssertions_lists() {
        val client = DesignClient()

        val actualDesigns = client.getAllDesigns()

        assertThat(actualDesigns).usingElementComparatorIgnoringFields("dateCreated").containsExactly(
            Design(
                id = 1,
                userId = 9,
                name = "Cat"
            ),
            Design(
                id = 2,
                userId = 4,
                name = "Dog"
            )
        )
        assertThat(actualDesigns).usingElementComparatorOnFields("userId", "name").containsExactly(
            Design(
                id = 1,
                userId = 9,
                name = "Cat"
            ),
            Design(
                id = 2,
                userId = 4,
                name = "Dog"
            )
        )
    }

    @Test
    fun sophisticatedAssertions_lists_kotest() {
        val client = DesignClient()

        val actualDesigns = client.getAllDesigns()

        // TODO doesn't seem to exist in kotest yet
//        assertThat(actualDesigns).usingElementComparatorIgnoringFields("dateCreated").containsExactly(
//            Design(id = 1, userId = 9, name = "Cat", dateCreated = Instant.ofEpochSecond(1518278198)),
//            Design(id = 2, userId = 4, name = "Dogggg", dateCreated = Instant.ofEpochSecond(1518279000))
//        )
//        assertThat(actualDesigns).usingElementComparatorOnFields("id", "name").containsExactly(
//            Design(id = 1, userId = 9, name = "Cat", dateCreated = Instant.ofEpochSecond(1518278198)),
//            Design(id = 2, userId = 4, name = "Dogggg", dateCreated = Instant.ofEpochSecond(1518279000))
//        )
    }

    @Test
    fun grouping() {
        val client = DesignClient()

        val actualDesign = client.requestDesign(id = 1)

        actualDesign.asClue {
            it.id shouldBe 2
            it.userId shouldBe 9
            it.name shouldBe "Cat"
        }
        /**
         * org.opentest4j.AssertionFailedError: Design(id=1, userId=9, name=Cat, dateCreated=2018-02-10T15:56:38Z)
        expected:<2> but was:<1>
        Expected :2
        Actual   :1
         */
    }
}

data class Design(
    val id: Int,
    val userId: Int,
    val name: String
)

class DesignClient {
    fun requestDesign(id: Int) =
        Design(
            id = 1,
            userId = 9,
            name = "Cat"
        )

    fun getAllDesigns() = listOf(
        Design(
            id = 1,
            userId = 9,
            name = "Cat"
        ),
        Design(
            id = 2,
            userId = 4,
            name = "Dogggg"
        )
    )
}