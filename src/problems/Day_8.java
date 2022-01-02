package problems;

import helpers.Helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Day_8 {
    public static long problem(String path) {
        Collection<String> inputLines = Helper.readFile(path);
        Program program = createProgram(inputLines);
        int runPure = program.run();
        return program.fixProgramToTerminate();
    }

    private static Program createProgram(Collection<String> inputLines) {
        List<Instruction> instructions = inputLines.stream()
                .map(Day_8::parseInstruction)
                .collect(Collectors.toList());
        return new Program(instructions);
    }

    private static Instruction parseInstruction(String i) {
        String[] split = i.split(" ");
        Instruction instruction = createInstruction(split[0]);
        return parseInput(split[1], instruction);
    }

    private static Instruction parseInput(String s, Instruction instruction) {
        String signString = s.substring(0, 1);
        int sign = signString.equals("+") ? 1 : -1;
        int input = Integer.parseInt(s.substring(1));
        return instruction.init(sign, input);
    }

    private static Instruction createInstruction(String instructionType) {
        switch (instructionType) {
            case "acc":
                return new Accumulator();
            case "nop":
                return new NoOp();
            case "jmp":
                return new JumpInstructor();
            default:
                throw new RuntimeException();
        }
    }

    private static class Program {
        List<Instruction> instructions;
        int accumulator = 0;
        int currentInstruction = 0;
        List<Integer> pastInstructions = new ArrayList<>();

        public Program(List<Instruction> instructions) {
            this.instructions = instructions;
        }

        public boolean addToIndex(int i) {
            currentInstruction += i;
            boolean instructionAlreadyRan = pastInstructions.contains(currentInstruction);
            return !instructionAlreadyRan;
        }

        public void addToAccumulator(int i) {
            this.accumulator += i;
        }

        public int run() {
            runTillLoop();
            //We want to return 1 indexed.
            return accumulator;
        }

        private void runTillLoop() {
            boolean running = true;
            while (running && currentInstruction < instructions.size()) {
                pastInstructions.add(currentInstruction);
                Instruction instruction = instructions.get(currentInstruction);
                running = instruction.act(this);
            }
        }

        public boolean runTerminates(int i, Instruction oldInstruction, Instruction newInstruction) {
            instructions.set(i, newInstruction);
            this.run();
            if (currentInstruction == instructions.size()) {
                return true;
            } else {
                //Reset instruction and values.
                instructions.set(i, oldInstruction);
                this.reset();
                return false;
            }
        }

        public int fixProgramToTerminate() {
            for (int i = 0; i < instructions.size(); i++) {
                Instruction instruction = instructions.get(i);
                if (instruction instanceof Accumulator) {
                    continue;
                }
                Instruction newInstruction = switchInstruction(instruction);
                if (this.runTerminates(i, instruction, newInstruction)) {
                    return accumulator;
                }
            }
            return 0;
        }

        private Instruction switchInstruction(Instruction instruction) {
            Instruction newInstructions;
            if (instruction instanceof NoOp) {
                newInstructions = new JumpInstructor();
            } else {
                newInstructions = new NoOp();
            }
            newInstructions.init(instruction.sign, instruction.input);
            return newInstructions;
        }

        private void reset() {
            accumulator = 0;
            currentInstruction = 0;
            pastInstructions.clear();
        }
    }

    private abstract static class Instruction {
        int sign;
        int input;

        public abstract boolean act(Program program);

        public Instruction init(int sign, int input) {
            this.sign = sign;
            this.input = input;
            return this;
        }
    }

    public static class Accumulator extends Instruction {
        public boolean act(Program program) {
            boolean canRun = program.addToIndex(1);
            if (canRun) {
                //Only accumulate if instruction can run. We are interested in the state after last valid instruction.
                program.addToAccumulator(sign * input);
            }
            return canRun;
        }
    }

    public static class NoOp extends Instruction {
        public boolean act(Program program) {
            return program.addToIndex(1);
        }

    }

    public static class JumpInstructor extends Instruction {
        public boolean act(Program program) {
            return program.addToIndex(sign * input);
        }
    }

}
