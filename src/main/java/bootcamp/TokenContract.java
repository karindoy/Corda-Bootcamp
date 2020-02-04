package bootcamp;

import net.corda.core.contracts.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import org.bouncycastle.crypto.Signer;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract {
    public static String ID = "bootcamp.TokenContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        //Shape
        if (tx.getInputStates().size() !=0)
            throw new IllegalArgumentException("Registro nao deve ter inputs.");
        if (tx.getOutputStates().size() !=1)
            throw new IllegalArgumentException("Registro deve retornar uma saída.");
        if (tx.getCommands().size()!=1)
            throw new IllegalArgumentException("Deve conter um comando.");

        //commands
//        if (!(tx.getOutput(0).getClass().equals(TokenState.class)))
//            throw new IllegalArgumentException("output deve ser um tokenstate");
//
//
        ContractState output = tx.getOutput(0);
        Command command = tx.getCommand(0);

        if (!(output instanceof TokenState))
            throw new IllegalArgumentException("output deve ser um tokenstate");
        if (!(command.getValue() instanceof Commands.Issue))
            throw new IllegalArgumentException("comando deve ser um Issue");

         TokenState tokenOutput = (TokenState) output;
        if (tokenOutput.getAmount() <= 0)
            throw new IllegalArgumentException("output nao deve ser 0 ou negativo: ");

//        CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);
//        if (!(command.getValue() instanceof Commands.Issue))
//            throw new IllegalArgumentException("comando deve ser um Issue");
//
//         TokenState tokenOutput = tx.outputsOfType(TokenState.class).get(0);
//        if (tokenOutput.getAmount() <= 0)
//            throw new IllegalArgumentException("output nao deve ser 0 ou negativo: ");




        List<PublicKey> listsigner = command.getSigners();
        PublicKey issuerKey = tokenOutput.getIssuer().getOwningKey();

        if (!(listsigner.contains(issuerKey)))
            throw new IllegalArgumentException("Output tem que ter asssiatura do Issuer.");

//        final TokenState tokenStateOutput = tx.outputsOfType(TokenState.class).get(0);
//        if (!(listsigner.contains(tokenStateOutput.getIssuer().getOwningKey())))
//            throw new IllegalArgumentException("Output tem que ter um assinante.");

    }


    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}