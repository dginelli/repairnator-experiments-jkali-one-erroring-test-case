package org.wotopul

import org.wotopul.VarValue.*

fun arrlen(array: VarValue): IntT {
    val size = when (array) {
        is UnboxedArrayT -> array.size
        is BoxedArrayT -> array.size
        else -> throw ExecutionException("cannot cast a value of type ${array.type()} to array")
    }
    return IntT(size)
}

fun arrmake(length: IntT, value: IntT): UnboxedArrayT {
    val array = Array(length.toInt(), { value.toInt() })
    return UnboxedArrayT(array)
}

fun Arrmake(length: IntT, initializer: BoxedArrayT): BoxedArrayT {
    val array = Array<ReferenceT?>(length.asIntT().toInt(), { null })
    for ((i, initItem) in initializer.value!!.withIndex()) {
        array[i] = initItem
    }
    return BoxedArrayT(array)
}

private val arrayIntrinsicNArgs = mapOf(
    "arrlen" to 1,
    "arrmake" to 2,
    "Arrmake" to 2
)

fun arrayIntrinsics() = arrayIntrinsicNArgs.keys

fun arrayIntrinsicNArgs(name: String): Int = arrayIntrinsicNArgs[name]!!

fun arrayIntrinsicWrapperName(name: String) = "_${name}_wrapper"