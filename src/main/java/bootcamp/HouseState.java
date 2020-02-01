package bootcamp;

import com.google.common.collect.ImmutableList;
import jdk.nashorn.internal.ir.annotations.Immutable;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HouseState implements ContractState {

    private String address;
    private Party owner;

    public Party getOwner() {
        return owner;
    }

    public void setOwner(Party owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public HouseState(String address, Party owner){
        this.address = address;
        this.owner = owner;
    }

    public static void main(String[] args) {
        Party joel = null;
        HouseState state = new HouseState("1Low Moor Avenue", joel);
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(owner);
    }
}
