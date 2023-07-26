import { fireEvent, render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { useState } from 'react';
import useKillingPartInterval from '../KillingPartToggleGroup/hooks/useKillingPartInterval';
import IntervalInput from './IntervalInput';
import type { TimeMinSec } from './IntervalInput.type';

const TestIntervalInput = () => {
  const videoLength = 210;
  const [partStart, setPartStart] = useState<TimeMinSec>({ minute: 0, second: 0 });
  const [errorMessage, setErrorMessage] = useState('');
  const { interval } = useKillingPartInterval();

  const onChangePartStart = (name: string, value: number) => {
    setPartStart({
      ...partStart,
      [name]: Number(value),
    });
  };

  const onChangeErrorMessage = (message: string) => {
    setErrorMessage(message);
  };

  return (
    <IntervalInput
      videoLength={videoLength}
      partStart={partStart}
      interval={interval}
      errorMessage={errorMessage}
      onChangePartStart={onChangePartStart}
      onChangeErrorMessage={onChangeErrorMessage}
    />
  );
};

describe('<IntervalInput /> 컴포넌트 테스트', () => {
  // 3분 30초

  test('4개의 input이 모두 그려진다.', () => {
    render(<TestIntervalInput />);
    const inputs = screen.getAllByRole('textbox');

    expect(inputs).toHaveLength(4);
  });

  test('start 2분 10초면, end는 2분 20초다. ', () => {
    render(<TestIntervalInput />);
    const [startMin, startSec, endMin, endSec] = screen.getAllByRole<HTMLInputElement>('textbox');

    fireEvent.change(startMin, { target: { value: '2' } });
    fireEvent.change(startSec, { target: { value: '10' } });

    expect(endMin.value).toBe('2');
    expect(endSec.value).toBe('20');
  });

  test('start 1분 55초면, end는 2분 5초다. ', () => {
    render(<TestIntervalInput />);
    const [startMin, startSec, endMin, endSec] = screen.getAllByRole<HTMLInputElement>('textbox');

    fireEvent.change(startMin, { target: { value: '1' } });
    fireEvent.change(startSec, { target: { value: '55' } });

    expect(endMin.value).toBe('2');
    expect(endSec.value).toBe('5');
  });

  test('start(3:25)가 노래길이에서 구간길이를 뺀 것(3:20)보다 길면 에러메세지를 띄운다.', async () => {
    const user = userEvent.setup();

    render(<TestIntervalInput />);
    const [startMin, startSec] = screen.getAllByRole<HTMLInputElement>('textbox').slice(0, 2);

    await user.type(startMin, '3');
    await user.type(startSec, '25');
    fireEvent.blur(startSec);

    const errorMessage = screen.getByRole('alert');

    expect(errorMessage.textContent).toBeTruthy();
  });
});