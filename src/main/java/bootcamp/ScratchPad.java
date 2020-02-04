package bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.Party;
import net.corda.core.transactions.TransactionBuilder;

import java.security.PublicKey;
import java.util.List;

public class ScratchPad {
    public static void main(String[] args) {
        StateAndRef<ContractState> inputState = null;
        HouseState outputState = new HouseState("123 endereco", null);
        PublicKey publicKeySigner = outputState.getOwner().getOwningKey();
        List<PublicKey> requiredSigners = ImmutableList.of(publicKeySigner);
        Party notary = null;

        TransactionBuilder txbuilder = new TransactionBuilder();

        txbuilder.setNotary(notary);
        txbuilder.addInputState(inputState)
                .addOutputState(outputState, "java_bootcamp.HouseContract")
                .addCommand(new HouseContract.Register(), requiredSigners);
    }
}
