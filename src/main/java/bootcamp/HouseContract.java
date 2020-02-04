package bootcamp;

import afu.org.checkerframework.checker.igj.qual.I;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

public class HouseContract implements Contract {


    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {

        if (tx.getCommands().size()!=1)
            throw new IllegalArgumentException("Transaction must have one command");

        Command command = tx.getCommand(0);
        List<PublicKey> requiredSigners = command.getSigners();
        CommandData commandType = command.getValue();

        if (commandType instanceof Register){
            //Quantidade de entradas e saidas
            if (tx.getInputStates().size() != 0)
                throw new IllegalArgumentException("Registro não pode ter inputs.");
            if (tx.getOutputStates().size() != 1)
                throw new IllegalArgumentException("Registro deve ter uma saida.");

            //Conteúdo das restrições
            ContractState outputState = tx.getOutput(0);//TODO 'pq passar o index'
            if (!(outputState instanceof HouseState))
                throw new IllegalArgumentException("Saída deve ser um HouseState.");

            HouseState houseState = (HouseState) outputState;
            if (houseState.getAddress().length() <= 3)
                throw new IllegalArgumentException("Endereço deve ter pelo menos 3 caracteres.");
            if (houseState.getOwner().getName().getCountry().equals("Brazil"))
                throw new IllegalArgumentException("Não é permitio ter proprietários brasileiros.");

            //Assinaturas requeridas
            Party owner = houseState.getOwner();
            PublicKey publicKey = owner.getOwningKey();
            if (!(requiredSigners.contains(publicKey)))
                throw new IllegalArgumentException("O dono deve assinar o registro.");

        } else if (commandType instanceof Transfer){
            //Quantidade de entradas e saidas
            if (tx.getInputStates().size() != 1)
                throw new IllegalArgumentException("Deve haver um input");
            if (tx.getOutputStates().size() != 1)
                throw new IllegalArgumentException("Deve haver um output");

            //Conteúdo das restrições
            ContractState input = tx.getInput(0);
            ContractState output = tx.getOutput(0);

            if (!(input instanceof HouseState))
                throw new IllegalArgumentException("Input deve ser um HouseState");
            if (!(output instanceof HouseState))
                throw new IllegalArgumentException("Output deve ser um HouseState");

            HouseState inputHouse = (HouseState) input;
            HouseState outputHouse = (HouseState) output;

            if (!(inputHouse.getAddress().equals(outputHouse.getAddress())))
                throw new IllegalArgumentException("Em uma transferencia o endereço nao pode mudar");
            if (inputHouse.getOwner().equals(outputHouse.getOwner()))
                throw new IllegalArgumentException("Em uma transferencia o dono deve mudar");

            Party inputOwner = inputHouse.getOwner();
            Party outputOwner = outputHouse.getOwner();
            //Assinaturas requeridas
            if (!(requiredSigners.contains(inputOwner.getOwningKey())))
                throw new IllegalArgumentException("O dono atual deve assinar.");
            if (!(requiredSigners.contains(outputOwner.getOwningKey())))
                throw new IllegalArgumentException("O novo dono atual deve assinar.");
        }else{
            throw new IllegalArgumentException("Command type not recognised.");
        }
    }

    public static class Register implements CommandData{}
    public static class Transfer implements CommandData{}
}
