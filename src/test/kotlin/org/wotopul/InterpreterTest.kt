package org.wotopul

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.wotopul.Configuration.OutputItem
import org.wotopul.Configuration.OutputItem.Number
import org.wotopul.Configuration.OutputItem.Prompt
import org.wotopul.Expr.Const
import org.wotopul.Expr.Variable
import org.wotopul.Statement.Skip
import org.wotopul.Statement.Write
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class InterpreterTest(val program: Program, val input: List<Int>, val output: List<OutputItem>) {
    companion object {
        @JvmStatic
        val SEQUENCE_OF_WRITES = programOf(sequence(
            Write(Const(41)),
            Write(Const(42)),
            Write(Const(43))
        ))

        @JvmStatic
        val SIMPLE_ARITHMETIC = programOf(Write(
            (Const(5) / Const(2) + Const(2)) * Const(42)
        ))

        @JvmStatic
        val WRITES_AND_READS = programOf(sequence(
            assignment("x", Const(1)),
            Write(Variable("x")),
            assignment("x", Const(2)),
            assignment("y", Const(3)),
            Write(Variable("y")),
            Write(Variable("x")),
            read("y"),
            read("y"),
            Write(Variable("x")),
            Write(Variable("y")),
            read("y"),
            Write(Variable("y"))
        ))

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> = listOf(
            arrayOf(programOf(Skip), listOf(1, 2, 3), emptyList<Int>()),
            arrayOf(SEQUENCE_OF_WRITES, emptyList<Int>(), listOf(41, 42, 43).map(::Number)),
            arrayOf(SIMPLE_ARITHMETIC, emptyList<Int>(), listOf(Number((5 / 2 + 2) * 42))),
            arrayOf(WRITES_AND_READS, listOf(7, 42, 0), listOf(
                Number(1), Number(3), Number(2),
                Prompt, Prompt, Number(2),
                Number(42), Prompt, Number(0)
            ))
        )
    }

    @Test
    fun testImpl() {
        assertEquals(output, interpret(program, input))
    }
}
