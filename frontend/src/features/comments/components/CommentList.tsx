import { useEffect, useState } from 'react';
import styled, { css } from 'styled-components';
import cancleIcon from '@/assets/icon/cancle.svg';
import shookshook from '@/assets/icon/shookshook.svg';
import BottomSheet from '@/shared/components/BottomSheet/BottomSheet';
import useModal from '@/shared/components/Modal/hooks/useModal';
import Spacing from '@/shared/components/Spacing';
import SRHeading from '@/shared/components/SRHeading';
import useToastContext from '@/shared/components/Toast/hooks/useToastContext';
import useFetch from '@/shared/hooks/useFetch';
import { useMutation } from '@/shared/hooks/useMutation';
import fetcher from '@/shared/remotes';
import Comment from './Comment';
import type React from 'react';

interface Comment {
  id: number;
  content: string;
  createdAt: string;
}

interface CommentListProps {
  songId: string;
  partId: number;
}

const CommentList = ({ songId, partId }: CommentListProps) => {
  const [newComment, setNewComment] = useState('');
  const { isOpen, openModal, closeModal } = useModal(false);

  const { data: comments, fetchData: getComment } = useFetch<Comment[]>(() =>
    fetcher(`/songs/${songId}/parts/${partId}/comments`, 'GET')
  );

  const { mutateData } = useMutation(() =>
    fetcher(`/songs/${songId}/parts/${partId}/comments`, 'POST', { content: newComment.trim() })
  );
  const { showToast } = useToastContext();

  const resetNewComment = () => setNewComment('');

  const changeNewComment: React.ChangeEventHandler<HTMLInputElement> = ({
    currentTarget: { value },
  }) => setNewComment(value);

  const submitNewComment: React.FormEventHandler<HTMLFormElement> = async (event) => {
    event.preventDefault();

    await mutateData();

    showToast('댓글이 등록되었습니다.');
    resetNewComment();
    getComment();
  };

  useEffect(() => {
    getComment();
  }, [partId]);

  if (!comments) {
    return null;
  }

  return (
    <>
      <Spacing direction="vertical" size={24} />
      <SRHeading as="h3">댓글 목록</SRHeading>
      <CommentTitle>댓글 {comments.length}개</CommentTitle>
      <Spacing direction="vertical" size={12} />
      <CommentWrapper onClick={openModal}>
        <Comment
          key={comments[0].id}
          content={comments[0].content}
          createdAt={comments[0].createdAt}
        />
      </CommentWrapper>
      <BottomSheet isOpen={isOpen} closeModal={closeModal}>
        <Spacing direction="vertical" size={16} />
        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
          <CommentsTitle>댓글 {comments.length}개</CommentsTitle>
          <CloseImg src={cancleIcon} onClick={closeModal} />
        </div>
        <Spacing direction="vertical" size={20} />
        <Comments>
          {comments.map(({ id, content, createdAt }) => (
            <Comment key={id} content={content} createdAt={createdAt} />
          ))}
        </Comments>
        <CommentForm onSubmit={submitNewComment}>
          <Flex>
            <Profile>
              <img src={shookshook} alt="익명 프로필" />
            </Profile>
            <Input
              type="text"
              value={newComment}
              onChange={changeNewComment}
              placeholder="댓글 추가..."
              maxLength={200}
            />
          </Flex>
          <FlexEnd>
            <Cancel type="button" onClick={resetNewComment} aria-label="댓글 작성 취소">
              취소
            </Cancel>
            <Submit aria-label="댓글 작성 완료" disabled={newComment.trim() === ''}>
              댓글
            </Submit>
          </FlexEnd>
        </CommentForm>
      </BottomSheet>
    </>
  );
};

export default CommentList;

const Flex = styled.div`
  display: flex;
  gap: 14px;
  align-items: flex-start;
`;

const Profile = styled.div`
  overflow: hidden;

  width: 40px;
  height: 40px;

  background-color: white;
  border-radius: 100%;
`;

const Input = styled.input`
  flex: 1;

  margin: 0;
  padding: 0;

  font-size: 14px;

  background-color: transparent;
  border: none;
  border-bottom: 1px solid white;
  outline: none;
  -webkit-box-shadow: none;
  box-shadow: none;
`;

const FlexEnd = styled.div`
  display: flex;
  gap: 10px;
  justify-content: flex-end;
`;

const buttonBase = css`
  width: 50px;
  height: 36px;
  font-size: 14px;
  border-radius: 10px;
`;

const Cancel = styled.button`
  ${buttonBase}

  &:hover,
  &:focus {
    background-color: ${({ theme }) => theme.color.secondary};
  }
`;

const Submit = styled.button`
  ${buttonBase};
  background-color: ${({ theme }) => theme.color.primary};

  &:hover,
  &:focus {
    background-color: #de5484;
  }

  &:disabled {
    background-color: ${({ theme }) => theme.color.secondary};
  }
`;

const Comments = styled.ol`
  gap: 10px;
  overflow-y: auto;
  max-height: calc (100vh - 100px);
  margin-bottom: 100px;
  padding: 0 16px;
`;

const CommentWrapper = styled.div`
  background-color: ${({ theme }) => theme.color.secondary};
  border-radius: 4px;
  padding: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const CommentTitle = styled.p`
  font-size: 20px;
`;

const CommentForm = styled.form`
  position: fixed;

  bottom: 0;
  z-index: 1;
  border-top: 1px solid ${({ theme }) => theme.color.white};
  padding: 16px;
  width: 100%;
  background-color: ${({ theme }) => theme.color.black};
`;

const CommentsTitle = styled.p`
  padding-left: 16px;
`;

const CloseImg = styled.img`
  width: 24px;
  height: 24px;
  right: 16px;
  position: fixed;
`;
