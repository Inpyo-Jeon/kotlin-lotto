package lotto.domain.entity.user

import lotto.domain.entity.error.LottoExceptionMessage

class Lotto(
    private val lottoNumber: List<Int>
) {

    init {
        require(lottoNumber.distinct().size == LOTTO_NUMBER_COUNT) { LottoExceptionMessage.LOTTO_NUMBER_OVERLAP }
    }

    fun getLottoNumber(): List<Int> = lottoNumber
        .sorted()
        .toList()

    companion object {
        private const val LOTTO_NUMBER_COUNT = 6
    }
}