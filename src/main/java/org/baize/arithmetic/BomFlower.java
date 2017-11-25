package org.baize.arithmetic;
import org.baize.EnumType.CardType;
import org.baize.logic.card.data.Card;
import java.util.Arrays;

/**
 * 作者： 白泽
 * 时间： 2017/11/24.
 * 描述：扎金花算法
 */
public class BomFlower {
    public static CardType checkType(Card card){
        if(checkAaa(card))
            return CardType.AAA;
        else if(checkBomb(card))
            return CardType.Bomb;
        else if(checkAlikeColorAndStaright(card))
            return CardType.AlikeColorAndStaright;
        else if(checkAlikeColor(card))
            return CardType.AlikeColor;
        else if(checkStraight(card))
            return CardType.Staright;
        else if(checkBoth(card))
            return CardType.Both;
        else
            return CardType.Scattered;
    }

    /**
     * AAA
     * @param card
     * @return
     */
    private static boolean checkAaa(Card card){
        if(checkAaa(card)){
            if(card.getCardIds()[0] == 1)
                return true;
        }
        return false;
    }

    /**
     * 炸
     * @param card
     * @return
     */
    private static boolean checkBomb(Card card){
        int[] cardNum = card.getCardIds();
        Arrays.sort(cardNum);
        for (int i = 0;i<cardNum.length-1;i++){
            if(cardNum[i] != cardNum[i+1])
                return false;
        }
        return true;
    }

    /**
     * 同花顺
     * @param card
     * @return
     */
    private static boolean checkAlikeColorAndStaright(Card card){
        if(!checkAlikeColor(card) && !checkStraight(card))
            return false;
        return true;
    }
    /**
     * 同花
     * @return
     */
    private static boolean checkAlikeColor(Card card){
        int[] cardTypem = card.getCardTypes();
        for (int i = 0;i<cardTypem.length-1;i++){
            if(cardTypem[i] != cardTypem[i+1])
                return false;
        }
        return true;
    }

    /**
     * 顺子
     */
    private static boolean checkStraight(Card card){
        int[] cardNum = card.getCardIds();
        Arrays.sort(cardNum);
        for (int i= 0;i<cardNum.length-1;i++){
            if(cardNum[i] != (cardNum[i+1]+1))
                return false;
        }
        return true;
    }

    /**
     * 对子
     * @param card
     * @return
     */
    private static boolean checkBoth(Card card){
        int[] cardNum = card.getCardIds();
        if(cardNum[1] == cardNum[0] || cardNum[1] == cardNum[2])
            return true;
        return false;
    }

    /**===============================================================================比大小=========================================================================*/
    private static Card banker;
    public static void compareTo(Card bankerx,Card other){
        banker = bankerx;
    }

    /**
     * 比较aaa，炸
     * @return
     */
    private static boolean bombSize(Card other){
        int[] bankerCard = banker.getCardIds();
        int[] otherCard = other.getCardIds();
        if(bankerCard[0] == 1)
            return false;
        else if(otherCard[0] == 1)
            return true;
        else if(bankerCard[bankerCard.length -1 ] < otherCard[otherCard.length - 1])
            return true;
        else
            return false;
    }

    /**
     * 顺子，同花顺
     * @param other
     * @return
     */
    private static boolean straightSize(Card other){
        int[] bankerCard = banker.getCardIds();
        int[] otherCard = other.getCardIds();
        Arrays.sort(bankerCard);
        Arrays.sort(otherCard);
        if(bankerCard[bankerCard.length-1] < otherCard[otherCard.length-1])
            return true;
        //比花色
        if(bankerCard[bankerCard.length-1] == otherCard[otherCard.length-1]){
            if(other.getTypeById(otherCard[otherCard.length-1]) < bankerCard[bankerCard.length-1])
                return true;
        }
        return false;
    }

    /**
     * 对子比较
     * @param other
     * @return
     */
    private static boolean bothSize(Card other){
        int[] bankerCard = banker.getCardIds();
        int[] otherCard = other.getCardIds();
        Arrays.sort(bankerCard);
        Arrays.sort(otherCard);

        int otherBoth = otherCard[1];
        int otherOne = 0;
        int bankerBoth = bankerCard[1];
        int bankerOne = 0;
        if(bankerCard[2] == bankerCard[1])
            bankerOne = bankerCard[0];
        else
            bankerOne = bankerCard[2];
        if(otherCard[1] == otherCard[2])
            otherOne = otherCard[0];
        else
            otherOne = otherCard[2];
        if(otherBoth > bankerBoth)
            return true;
        if(otherBoth == bankerBoth) {
            if (otherOne > bankerOne)
                return true;
            else if(otherOne == bankerOne){//都相同比散牌花色
                if(other.getTypeById(otherOne) < banker.getTypeById(bankerOne))
                    return true;
            }
        }
        return false;
    }

    /**
     * 比较散牌
     * @param other
     * @return
     */
    public static boolean scatteredSize(Card banker,Card other){
        int[] bankerCard = banker.getCardIds();
        int[] otherCard = other.getCardIds();
        Arrays.sort(bankerCard);
        Arrays.sort(otherCard);

        if(otherCard[otherCard.length-1] > bankerCard[bankerCard.length-1])
            return true;
        if(otherCard[otherCard.length-1] == bankerCard[bankerCard.length-1]){
            if(otherCard[otherCard.length-2] > bankerCard[bankerCard.length-2])
                return true;
            else if(otherCard[otherCard.length-2] == bankerCard[bankerCard.length-2]){
                if(otherCard[otherCard.length-3] > bankerCard[bankerCard.length-3])
                    return true;
                else if(otherCard[otherCard.length-3] == bankerCard[bankerCard.length-3])
                    if(other.getTypeById(otherCard[otherCard.length-1]) < banker.getTypeById(bankerCard[bankerCard.length-1]))
                        return true;
            }
        }
        return false;
    }
}
