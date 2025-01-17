import { useState } from 'react';
import { css, styled } from 'styled-components';
import { secondsToMinSec } from '@/shared/utils/convertTime';
import { isValidMinSec } from '@/shared/utils/validateTime';
import ERROR_MESSAGE from '../constants/errorMessage';
import useVoteInterfaceContext from '../hooks/useVoteInterfaceContext';
import { isInputName } from '../types/IntervalInput.type';
import type { IntervalInputType } from '../types/IntervalInput.type';

export interface IntervalInputProps {
  errorMessage: string;
  onChangeErrorMessage: (message: string) => void;
}

const IntervalInput = ({ errorMessage, onChangeErrorMessage }: IntervalInputProps) => {
  const { interval, partStartTime, videoLength, updatePartStartTime } = useVoteInterfaceContext();

  const [activeInput, setActiveInput] = useState<IntervalInputType>(null);

  const partEndTime = partStartTime + interval;
  const { minute: startMinute, second: startSecond } = secondsToMinSec(partStartTime);
  const { minute: endMinute, second: endSecond } = secondsToMinSec(partEndTime);

  const onChangeIntervalStart: React.ChangeEventHandler<HTMLInputElement> = ({
    currentTarget: { name: timeUnit, value, valueAsNumber },
  }) => {
    if (!isValidMinSec(value)) {
      onChangeErrorMessage(ERROR_MESSAGE.MIN_SEC);

      return;
    }

    onChangeErrorMessage('');
    updatePartStartTime(timeUnit, valueAsNumber);
  };

  const onFocusIntervalStart: React.FocusEventHandler<HTMLInputElement> = ({
    currentTarget: { name },
  }) => {
    if (isInputName(name)) {
      setActiveInput(name);
    }
  };

  const onBlurIntervalStart = () => {
    if (partStartTime + interval > videoLength) {
      const { minute: songMin, second: songSec } = secondsToMinSec(videoLength - interval);

      onChangeErrorMessage(ERROR_MESSAGE.SONG_RANGE(songMin, songSec));
      return;
    }

    onChangeErrorMessage('');
    setActiveInput(null);
  };

  return (
    <IntervalContainer>
      <Flex>
        <InputStart
          name="minute"
          value={startMinute}
          onChange={onChangeIntervalStart}
          onBlur={onBlurIntervalStart}
          onFocus={onFocusIntervalStart}
          placeholder="0"
          autoComplete="off"
          inputMode="numeric"
          $active={activeInput === 'minute'}
        />
        <Separator>:</Separator>
        <InputStart
          name="second"
          value={startSecond}
          onChange={onChangeIntervalStart}
          onBlur={onBlurIntervalStart}
          onFocus={onFocusIntervalStart}
          placeholder="0"
          autoComplete="off"
          inputMode="numeric"
          $active={activeInput === 'second'}
        />
        <Separator> ~ </Separator>
        <InputEnd value={endMinute} disabled />
        <Separator $inactive>:</Separator>
        <InputEnd value={endSecond} disabled />
      </Flex>
      <ErrorMessage role="alert">{errorMessage}</ErrorMessage>
    </IntervalContainer>
  );
};

export default IntervalInput;

const IntervalContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;

  padding: 0 24px;

  font-size: 16px;
  color: ${({ theme: { color } }) => color.white};
`;

const Flex = styled.div`
  display: flex;
`;

const ErrorMessage = styled.p`
  margin: 8px 0;
  font-size: 12px;
  color: ${({ theme: { color } }) => color.error};
`;

const Separator = styled.span<{ $inactive?: boolean }>`
  flex: none;

  margin: 0 8px;
  padding-bottom: 8px;

  color: ${({ $inactive, theme: { color } }) => $inactive && color.subText};
  text-align: center;
`;

const inputBase = css`
  flex: 1;

  width: 16px;
  margin: 0 8px;
  margin: 0;
  padding: 0;

  text-align: center;

  background-color: transparent;
  border: none;
  border-bottom: 1px solid white;
  outline: none;
  -webkit-box-shadow: none;
  box-shadow: none;
`;

const InputStart = styled.input<{ $active: boolean }>`
  ${inputBase}
  color: ${({ theme: { color } }) => color.white};
  border-bottom: 1px solid
    ${({ $active, theme: { color } }) => ($active ? color.primary : color.white)};
`;

const InputEnd = styled.input`
  ${inputBase}
  color: ${({ theme: { color } }) => color.subText};
  border-bottom: 1px solid ${({ theme: { color } }) => color.subText};
`;
