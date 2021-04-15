package controller;

import exception.MyException;
import model.expressions.*;
import model.program.*;
import model.program.exestack.ExecutionStack;
import model.program.exestack.ExecutionStackInterface;
import model.program.filetable.FileTable;
import model.program.heap.Heap;
import model.program.output.Output;
import model.program.symbdict.SymbolTable;
import model.program.synchronization.LockTable;
import model.values.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ProgramFactory {
    private static final Map<Integer, Supplier<ProgramState>> programs = new HashMap<>();

    static{
        programs.put(1, ProgramFactory::getProgram1);
        programs.put(2, ProgramFactory::getProgram2);
        programs.put(3, ProgramFactory::getProgram3);
        programs.put(4, ProgramFactory::getProgram4);
        programs.put(5, ProgramFactory::getProgram5);
        programs.put(6, ProgramFactory::getProgram6);
        programs.put(7, ProgramFactory::getProgram7);
        programs.put(8, ProgramFactory::getProgram8);
        programs.put(9, ProgramFactory::getProgram9);
        programs.put(10, ProgramFactory::getProgram10);
    }

    public static ProgramState getProgram(Integer i){
        return programs.get(i).get();
    }


    public static ProgramState getProgram1()  {
        // program 1
        // assign 2+3*5 to a
        Statement aAssignmentStatement = new AssignmentStatement(
                "a", new ArithmeticExpression(
                new ConstantExpression(new IntValue(2)),
                new ArithmeticExpression(new ConstantExpression(new IntValue(3)),
                        new ConstantExpression(new IntValue(5)),
                        ArithmeticExpression.Operator.MUL),
                ArithmeticExpression.Operator.ADD));
        // assign a - 2 to b
        Statement bAssignmentStatement = new AssignmentStatement(
                "b",
                new ArithmeticExpression(
                        new VariableExpression("a"),
                        new ConstantExpression(new IntValue(2)),
                        ArithmeticExpression.Operator.SUB
                )
        );
        ExecutionStackInterface stack = new ExecutionStack(
                new CompoundStatement(new VariableDeclarationStatement(Type.Int,"a"), aAssignmentStatement),
                new CompoundStatement(new VariableDeclarationStatement(Type.Int, "b"), bAssignmentStatement),
                new PrintStatement(new VariableExpression("b"))
        );
        return new ProgramState(stack,new Output(),new SymbolTable(), new FileTable(), new Heap(), new LockTable());

    }

    public static ProgramState getProgram2()  {
        Statement ifStatement = new IfStatement(
                new VariableExpression("a"),
                new AssignmentStatement("v",new ConstantExpression(new IntValue(2))),
                new AssignmentStatement("v",new ConstantExpression(new IntValue(3)))

        );
        ExecutionStackInterface stack = new ExecutionStack(
                new VariableDeclarationStatement(Type.Str, "v"),
                new CompoundStatement(new VariableDeclarationStatement(Type.Bool,"a"),
                        new AssignmentStatement("a", new ConstantExpression(new BoolValue(true)))),
                ifStatement,
                new PrintStatement(new VariableExpression("v"))
        );

        return new ProgramState(stack,new Output(),new SymbolTable(), new FileTable(), new Heap(),  new LockTable());
    }

    public static ProgramState getProgram3()  {
        Expression file = new VariableExpression("file");
        ExecutionStackInterface stack = new ExecutionStack(
                new VariableDeclarationStatement(Type.Str,"file"),
                new AssignmentStatement("file",new ConstantExpression(new StringValue("src/view/numbers.txt"))),
                new VariableDeclarationStatement(Type.Int, "a"),
                new FileROpenStatement(file),
                new FileReadStatement(file,"a"),
                new PrintStatement(new VariableExpression("a")),
                new VariableDeclarationStatement(Type.Int, "b"),
                new FileReadStatement(file, "b"),
                new IfStatement(new RelationalExpression(
                        RelationalExpression.Operator.GT,
                        new VariableExpression("a"),
                        new VariableExpression("b")),
                        new PrintStatement(new ConstantExpression(new StringValue("a > b")))),
                new IfStatement(new RelationalExpression(
                        RelationalExpression.Operator.EQ,
                        new VariableExpression("b"),
                        new ConstantExpression(new IntValue(3))),
                        new PrintStatement(new ConstantExpression(new StringValue("b is 3")))),
                new FileCloseStatement(file)
        );

        return new ProgramState(stack,new Output(),new SymbolTable(), new FileTable(), new Heap(),  new LockTable());


    }

    public static ProgramState getProgram4() {
        //Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))

        ExecutionStackInterface stack = new ExecutionStack(
                new VariableDeclarationStatement(new ReferenceType(Type.Int),"v"),
                new MemoryAllocationStatement("v",new ConstantExpression(new IntValue(20))),
                new VariableDeclarationStatement(new ReferenceType(new ReferenceType(Type.Int)),"a"),
                new MemoryAllocationStatement("a", new VariableExpression("v")),
                new MemoryAllocationStatement("v", new ConstantExpression(new IntValue(30))),
                new PrintStatement(new DereferenceExpression(new DereferenceExpression(new VariableExpression("a")))),
                new PrintStatement(new DereferenceExpression(new VariableExpression("v")))
        );

        return new ProgramState(stack,new Output(),new SymbolTable(), new FileTable(), new Heap(),  new LockTable());
    }

    public static ProgramState getProgram5() {
        //Ref int v; Ref Ref Int w; new (v,2) new (w,v); (while (rh(v)>0) wH(v,v-1) print(rh(v));print(rH(v));
        // print(rH(rH(w)));
        VariableExpression v = new VariableExpression("v");
        VariableExpression w = new VariableExpression("w");
        DereferenceExpression dv = new DereferenceExpression(v);
        DereferenceExpression dw = new DereferenceExpression(w);
        CompoundStatement whileCode = new CompoundStatement(new PrintStatement(dv),new MemoryWriteStatement("v",
                new ArithmeticExpression(dv, new ConstantExpression(new IntValue(1)), ArithmeticExpression.Operator.SUB)));
        ExecutionStackInterface stack = new ExecutionStack(
                new VariableDeclarationStatement(new ReferenceType(Type.Int),"v"),
                new VariableDeclarationStatement(new ReferenceType(new ReferenceType(Type.Int)),"w"),
                new MemoryAllocationStatement("v",new ConstantExpression(new IntValue(2))),
                new MemoryAllocationStatement("w", v),
                new WhileStatement(
                        new RelationalExpression(RelationalExpression.Operator.GT, dv, new ConstantExpression(new IntValue(0))),
                        whileCode),
                new PrintStatement(dv),
                new PrintStatement(dw));

        return new ProgramState(stack,new Output(),new SymbolTable(), new FileTable(), new Heap(),  new LockTable());

    }

    public static ProgramState getProgram6() {
        /*int v; Ref int a; v=10;new(a,22);
            fork(wH(a,30);v=32;print(v);print(rH(a)));
            print(v);print(rH(a)) */
        VariableExpression v = new VariableExpression("v");
        VariableExpression a = new VariableExpression("a");
        CompoundStatement threadCode = new CompoundStatement(
                new MemoryWriteStatement("a", new ConstantExpression(new IntValue(30))),
                new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(new IntValue(32))),
                        new CompoundStatement(new PrintStatement(v),new PrintStatement(new DereferenceExpression(a))))
        );
        ExecutionStackInterface stack = new ExecutionStack(
                new VariableDeclarationStatement(Type.Int,"v"),
                new VariableDeclarationStatement(new ReferenceType(Type.Int), "a"),
                new AssignmentStatement("v", new ConstantExpression(new IntValue(10))),
                new MemoryAllocationStatement("a",new ConstantExpression(new IntValue(22))),
                new ForkStatement(threadCode),
                new CompoundStatement( new PrintStatement(v),
                        new PrintStatement(new DereferenceExpression(a))));

        return new ProgramState(stack,new Output(),new SymbolTable(), new FileTable(), new Heap(),  new LockTable());
    }

    public static ProgramState getProgram7(){
        Expression x = new VariableExpression("x");
        Expression c = new VariableExpression("c");
        Expression ten = new ConstantExpression(new IntValue(10));
        Expression one = new ConstantExpression(new IntValue(1));
        Statement divide_x = new AssignmentStatement("x",
                new ArithmeticExpression(x,ten, ArithmeticExpression.Operator.DIV));
        Statement increment_c = new AssignmentStatement("c",
                new ArithmeticExpression(c,one, ArithmeticExpression.Operator.ADD));
        Expression condition = new RelationalExpression(RelationalExpression.Operator.GT,x,new ConstantExpression(new IntValue(0)));
        ExecutionStack stack = new ExecutionStack(
                new VariableDeclarationStatement(Type.Int,"x"),
                new AssignmentStatement("x", new ConstantExpression(new IntValue(0))),
                new VariableDeclarationStatement(Type.Int,"c"),
                new AssignmentStatement("c", new ConstantExpression(new IntValue(0))),
                new DoWhileStatement(condition,new CompoundStatement(divide_x,increment_c)),
                new PrintStatement(c)
        );
        return new ProgramState(stack, new Output(), new SymbolTable(), new FileTable(), new Heap(),  new LockTable());
    }

    public static ProgramState getProgram8(){
        Expression i = new VariableExpression("i");
        Expression one = new ConstantExpression(new IntValue(1));
        Statement increment_i = new AssignmentStatement("i",
                new ArithmeticExpression(i,one, ArithmeticExpression.Operator.ADD));
        Expression condition = new RelationalExpression(RelationalExpression.Operator.LE, i, new ConstantExpression(new IntValue(4)));
        ExecutionStack stack = new ExecutionStack(
                new ForStatement(condition,
                        new CompoundStatement(new VariableDeclarationStatement(Type.Int,"i"), new AssignmentStatement("i",one)),
                        increment_i, new PrintStatement(i)));

        return new ProgramState(stack, new Output(), new SymbolTable(), new FileTable(), new Heap(),  new LockTable());
    }

    public static ProgramState getProgram9() {
        Expression a = new VariableExpression("a");
        Expression b = new VariableExpression("b");
        Expression x = new VariableExpression("x");
        Expression y = new VariableExpression("y");
        Expression a_gt_b = new RelationalExpression(RelationalExpression.Operator.GT, a, b);
        Expression a_eq_b = new RelationalExpression(RelationalExpression.Operator.EQ, a, b);
        ConstantExpression one = new ConstantExpression(new IntValue(1));
        ConstantExpression _one = new ConstantExpression(new IntValue(-1));
        ConstantExpression zero = new ConstantExpression(new IntValue(0));
        ConstantExpression forty2 = new ConstantExpression(new IntValue(42));

        Statement init_x = new CompoundStatement(new VariableDeclarationStatement(Type.Int, "x"),
                new AssignmentStatement("x", new ConditionalExpression(a_gt_b, one, _one)));
        Statement init_y = new CompoundStatement(new VariableDeclarationStatement(Type.Int, "y"),
                new AssignmentStatement("y", new ConditionalExpression(a_eq_b, zero, forty2)));
        Statement init_a = new CompoundStatement(new VariableDeclarationStatement(Type.Int, "a"),
                new AssignmentStatement("a", new ConstantExpression(new IntValue(2))));
        Statement init_b = new CompoundStatement(new VariableDeclarationStatement(Type.Int, "b"),
                new AssignmentStatement("b", new ConstantExpression(new IntValue(3))));

        Statement switch_x = new SwitchStatement(x,
                new SwitchStatement.Case(one,
                        new CompoundStatement(new PrintStatement(x),
                                new PrintStatement(new ConstantExpression(new StringValue("Branch 1"))))),
                new SwitchStatement.Case(_one,
                        new CompoundStatement(new PrintStatement(x),
                                new PrintStatement(new ConstantExpression(new StringValue("Branch -1"))))));

        Statement switch_y = new SwitchStatement(y,
                new CompoundStatement(new PrintStatement(y), new PrintStatement(new ConstantExpression(new StringValue("IDK")))),
                new SwitchStatement.Case(one,
                        new CompoundStatement(new PrintStatement(y),
                                new PrintStatement(new ConstantExpression(new StringValue("Branch 1"))))),
                new SwitchStatement.Case(_one,
                        new CompoundStatement(new PrintStatement(y),
                                new PrintStatement(new ConstantExpression(new StringValue("Branch -1"))))));

        ExecutionStack stack = new ExecutionStack(init_a, init_b, init_x, init_y, switch_x, switch_y);

        return new ProgramState(stack, new Output(), new SymbolTable(), new FileTable(), new Heap(),  new LockTable());
    }

    public static ProgramState getProgram10(){
        VariableDeclarationStatement declare_v1 = new VariableDeclarationStatement(
                new ReferenceType(Type.Int), "v1"
        );
        VariableDeclarationStatement declare_v2 = new VariableDeclarationStatement(
                new ReferenceType(Type.Int), "v2"
        );
        VariableDeclarationStatement declare_x = new VariableDeclarationStatement(Type.Int, "x");
        VariableDeclarationStatement declare_q = new VariableDeclarationStatement(Type.Int, "q");

        DeclareLockStatement newLockX = new DeclareLockStatement("x");
        AcquireLockStatement lockX = new AcquireLockStatement("x");
        ReleaseLockStatement unlockX = new ReleaseLockStatement("x");

        DeclareLockStatement newLockQ = new DeclareLockStatement("q");
        AcquireLockStatement lockQ = new AcquireLockStatement("q");
        ReleaseLockStatement unlockQ = new ReleaseLockStatement("q");

        MemoryAllocationStatement init_v1 = new MemoryAllocationStatement("v1",
                new ConstantExpression(new IntValue(20)));
        MemoryAllocationStatement init_v2 = new MemoryAllocationStatement("v2",
                new ConstantExpression(new IntValue(30)));

        VariableExpression v1 = new VariableExpression("v1");
        VariableExpression v2 = new VariableExpression("v2");
        VariableExpression x = new VariableExpression("x");
        VariableExpression q = new VariableExpression("q");

        MemoryWriteStatement updateV11 = new MemoryWriteStatement("v1",
                new ArithmeticExpression(new DereferenceExpression(v1),
                        new ConstantExpression(new IntValue(1)), ArithmeticExpression.Operator.SUB));

        MemoryWriteStatement updateV12 = new MemoryWriteStatement("v1",
                new ArithmeticExpression(new DereferenceExpression(v1),
                        new ConstantExpression(new IntValue(10)), ArithmeticExpression.Operator.MUL));

        ForkStatement innerFork1 = new ForkStatement(
                new CompoundStatement(lockX, new CompoundStatement(updateV11, unlockX)));
        ForkStatement fork1 = new ForkStatement(
                new CompoundStatement(innerFork1, new CompoundStatement(lockX,
                        new CompoundStatement(updateV12, unlockX)))
        );


        MemoryWriteStatement updateV21 = new MemoryWriteStatement("v2",
                new ArithmeticExpression(new DereferenceExpression(v2),
                        new ConstantExpression(new IntValue(5)), ArithmeticExpression.Operator.ADD));

        MemoryWriteStatement updateV22 = new MemoryWriteStatement("v2",
                new ArithmeticExpression(new DereferenceExpression(v2),
                        new ConstantExpression(new IntValue(10)), ArithmeticExpression.Operator.MUL));

        ForkStatement innerFork2 = new ForkStatement(
                new CompoundStatement(lockQ, new CompoundStatement(updateV21, unlockQ))
        );

        ForkStatement fork2 = new ForkStatement(new CompoundStatement(innerFork2,
                new CompoundStatement(lockQ, new CompoundStatement(
                        updateV22, unlockQ
                ))));
        NopStatement nop = new NopStatement();

        ExecutionStack stack = new ExecutionStack(
                declare_v1,
                declare_v2,
                declare_x,
                declare_q,
                init_v1,
                init_v2,
                newLockX,
                fork1,
                newLockQ,
                fork2,
                nop, nop, nop, nop,
                lockX,
                new PrintStatement(new DereferenceExpression(v1)),
                unlockX,
                lockQ,
                new PrintStatement(new DereferenceExpression(v2)),
                unlockQ
        );

        return new ProgramState(stack, new Output(), new SymbolTable(), new FileTable(), new Heap(),  new LockTable());

    }
}
