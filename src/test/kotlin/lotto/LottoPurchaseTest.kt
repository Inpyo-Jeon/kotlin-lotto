package lotto

import lotto.agency.LottoWinningEnum
import lotto.agency.LottoJudge
import lotto.exception.MinimumPurchaseMoneyException
import lotto.exception.NotNumericException
import lotto.exception.WonLottoNumberCountInconsistencyException
import lotto.seller.LottoSeller
import lotto.validation.LottoValidate
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LottoPurchaseTest {

    @Test
    fun `입력된 구입 금액만큼 로또를 n개 구입`() {
        val money = 13500
        val lottoSeller = LottoSeller()
        val lottoPurchaseAmount = lottoSeller.calculateLottoPurchaseAmount(money)
        val purchaseLottoTickets = lottoSeller.sell(lottoPurchaseAmount, lottoSeller.takeLottoNumbers())

        assertThat(purchaseLottoTickets.size).isEqualTo(lottoPurchaseAmount)
    }

    @Test
    fun `입력된 구입 금액이 1개를 구매할 수 있는 최소 금액이 넘지 않으면 에러처리`() {
        val money = "500"
        val lottoValidate = LottoValidate()

        Assertions.assertThatThrownBy { lottoValidate.validatePurchase(money) }
            .isInstanceOf(MinimumPurchaseMoneyException::class.java)
    }

    @Test
    fun `입력값이 숫자가 아니면 에러처리`() {
        val money = "string"
        val lottoValidate = LottoValidate()

        Assertions.assertThatThrownBy { lottoValidate.validatePurchase(money) }
            .isInstanceOf(NotNumericException::class.java)
    }

    @Test
    fun `지난주 당첨 번호 - 입력값이 숫자가 아니면 에러처리`() {
        val wonLottoLastWeek = listOf("1", "2", "3", "4", "5", "six")
        val lottoValidate = LottoValidate()

        Assertions.assertThatThrownBy { lottoValidate.validateWonLotto(wonLottoLastWeek) }
            .isInstanceOf(NotNumericException::class.java)
    }

    @Test
    fun `지난주 당첨 번호 - 당첨번호는 6자리이고, 6자리가 아니면 에러처리`() {
        val wonLottoLastWeek = listOf("1", "2", "3", "4", "5")
        val lottoValidate = LottoValidate()

        Assertions.assertThatThrownBy { lottoValidate.validateWonLotto(wonLottoLastWeek) }
            .isInstanceOf(WonLottoNumberCountInconsistencyException::class.java)
    }

    @Test
    fun `입력된 지난주 당첨 번호를 토대로 당첨 여부를 계산`() {
        val money = 1000
        val lottoSeller = LottoSeller()
        val wonLottoLastWeek = listOf(1, 2, 3, 4, 5, 6)
        val selectedLottoNumbers = listOf(5, 7, 11, 1, 21, 2)
        val lottoPurchaseAmount = lottoSeller.calculateLottoPurchaseAmount(money)
        val purchaseLottoTickets = lottoSeller.sell(lottoPurchaseAmount, selectedLottoNumbers)

        val lottoJudge = LottoJudge()
        val result = lottoJudge.determineWinning(purchaseLottoTickets, wonLottoLastWeek)

        assertThat(result[LottoWinningEnum.FOURTH_PLACE]).isEqualTo(1)
    }
}