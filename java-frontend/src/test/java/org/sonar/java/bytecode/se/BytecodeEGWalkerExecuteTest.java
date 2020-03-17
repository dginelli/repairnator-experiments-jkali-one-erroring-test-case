/*
 * SonarQube Java
 * Copyright (C) 2012-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.bytecode.se;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.objectweb.asm.Opcodes;

import org.sonar.java.bytecode.cfg.BytecodeCFGBuilder;
import org.sonar.java.bytecode.cfg.Instructions;
import org.sonar.java.se.ProgramState;
import org.sonar.java.se.constraint.Constraint;
import org.sonar.java.se.constraint.ConstraintsByDomain;
import org.sonar.java.se.constraint.ObjectConstraint;
import org.sonar.java.se.symbolicvalues.SymbolicValue;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BytecodeEGWalkerExecuteTest {

  @Test
  public void test_nop() throws Exception {
    ProgramState programState = execute(new Instructions().visitInsn(Opcodes.NOP));
    assertThat(programState).isEqualTo(ProgramState.EMPTY_STATE);
  }

  @Test
  public void test_ldc() throws Exception {
    ProgramState programState = execute(new Instructions().visitLdcInsn(0));
    assertStack(programState, ObjectConstraint.NOT_NULL);
  }

  @Test
  public void test_aconst_null() throws Exception {
    ProgramState programState = execute(new Instructions().visitInsn(Opcodes.ACONST_NULL));
    assertStack(programState, ObjectConstraint.NULL);
  }

  @Test
  public void test_areturn() throws Exception {
    SymbolicValue returnValue = new SymbolicValue();
    ProgramState programState = execute(new Instructions().visitInsn(Opcodes.ARETURN), ProgramState.EMPTY_STATE.stackValue(returnValue));
    assertThat(programState.peekValue()).isEqualTo(returnValue);
  }

  private void assertStack(ProgramState ps, Constraint... constraints) {
    assertThat(ps.stackDepth()).isEqualTo(1);
    List<SymbolicValue> symbolicValues = ps.peekValues(constraints.length);
    int idx = 0;
    for (SymbolicValue sv : symbolicValues) {
      ConstraintsByDomain constraintsByDomain = ps.getConstraints(sv);
      Constraint expectedConstraint = constraints[idx];
      if (expectedConstraint != null) {
        Class<? extends Constraint> expectedConstraintDomain = expectedConstraint.getClass();
        Constraint constraint = constraintsByDomain.get(expectedConstraintDomain);
        assertThat(constraint).isEqualTo(expectedConstraint);
        assertThat(constraintsByDomain.remove(expectedConstraintDomain).isEmpty()).isTrue();
      } else {
        assertThat(constraintsByDomain).isNull();
      }
      idx++;
    }
  }

  private ProgramState execute(Instructions instructions) {
    return execute(instructions, ProgramState.EMPTY_STATE);
  }

  private ProgramState execute(Instructions instructions, ProgramState startingState) {
    BytecodeCFGBuilder.BytecodeCFG cfg = instructions.cfg();
    BytecodeEGWalker walker = new BytecodeEGWalker();
    walker.execute(cfg, ImmutableList.of(startingState));
    return walker.programState;
  }
}
