/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.core.protocol.convertor;

import io.seata.core.exception.TransactionExceptionCode;
import io.seata.core.protocol.ResultCode;
import io.seata.core.protocol.protobuf.AbstractMessageProto;
import io.seata.core.protocol.protobuf.AbstractResultMessageProto;
import io.seata.core.protocol.protobuf.AbstractTransactionResponseProto;
import io.seata.core.protocol.protobuf.GlobalBeginResponseProto;
import io.seata.core.protocol.protobuf.MessageTypeProto;
import io.seata.core.protocol.protobuf.ResultCodeProto;
import io.seata.core.protocol.protobuf.TransactionExceptionCodeProto;
import io.seata.core.protocol.transaction.GlobalBeginResponse;

/**
 * @author leizhiyuan
 */
public class GlobalBeginResponseConvertor implements PbConvertor<GlobalBeginResponse, GlobalBeginResponseProto> {
    @Override
    public GlobalBeginResponseProto convert2Proto(GlobalBeginResponse globalBeginResponse) {
        final short typeCode = globalBeginResponse.getTypeCode();

        final AbstractMessageProto abstractMessage = AbstractMessageProto.newBuilder().setMessageType(
            MessageTypeProto.forNumber(typeCode)).build();

        final AbstractResultMessageProto abstractResultMessageProto = AbstractResultMessageProto.newBuilder().setMsg(
            globalBeginResponse.getMsg())
            .setResultCode(ResultCodeProto.valueOf(globalBeginResponse.getResultCode().name())).setAbstractMessage(
                abstractMessage).build();

        final AbstractTransactionResponseProto abstractTransactionRequestProto = AbstractTransactionResponseProto
            .newBuilder()
            .setAbstractResultMessage(abstractResultMessageProto)
            .setTransactionExceptionCode(
                TransactionExceptionCodeProto.valueOf(globalBeginResponse.getTransactionExceptionCode().name()))
            .build();

        GlobalBeginResponseProto result = GlobalBeginResponseProto.newBuilder().setAbstractTransactionResponse(
            abstractTransactionRequestProto)
            .setExtraData(globalBeginResponse.getExtraData())
            .setXid(globalBeginResponse.getXid()).build();
        return result;
    }

    @Override
    public GlobalBeginResponse convert2Model(GlobalBeginResponseProto globalBeginResponseProto) {
        GlobalBeginResponse branchCommitResponse = new GlobalBeginResponse();
        branchCommitResponse.setXid(globalBeginResponseProto.getXid());
        branchCommitResponse.setExtraData(
            globalBeginResponseProto.getExtraData());
        branchCommitResponse.setMsg(
            globalBeginResponseProto.getAbstractTransactionResponse()
                .getAbstractResultMessage().getMsg());
        branchCommitResponse.setResultCode(ResultCode.valueOf(
            globalBeginResponseProto.getAbstractTransactionResponse()
                .getAbstractResultMessage().getResultCode().name()));

        branchCommitResponse.setTransactionExceptionCode(TransactionExceptionCode.valueOf(
            globalBeginResponseProto.getAbstractTransactionResponse()
                .getTransactionExceptionCode().name()));
        return branchCommitResponse;
    }
}