import { styled } from 'styled-components';
import Thumbnail from './Thumbnail';

interface CardProps {
  rank: number;
  title: string;
  singer: string;
  albumCoverUrl: string;
  totalVoteCount: number;
}

const PopularSongItem = ({ rank, albumCoverUrl, title, singer, totalVoteCount }: CardProps) => {
  return (
    <Grid>
      <Rank>{rank}</Rank>
      <Thumbnail src={albumCoverUrl} alt={`${title}-${singer}`} />
      <SongTitle>{title}</SongTitle>
      <Singer>{singer}</Singer>
      <Info aria-label={`${totalVoteCount} 투표됨`}>
        {new Intl.NumberFormat('ko-KR').format(totalVoteCount)} votes
      </Info>
    </Grid>
  );
};

export default PopularSongItem;

const Grid = styled.div`
  display: grid;
  grid-template:
    'rank thumbnail title' 26px
    'rank thumbnail singer' 26px
    'rank thumbnail info' 18px
    / 14px 70px;
  column-gap: 8px;

  padding: 6px 0;

  color: ${({ theme: { color } }) => color.white};
`;

const Rank = styled.div`
  display: flex;
  grid-area: rank;
  align-items: center;
  justify-content: center;

  font-weight: 800;
`;

const SongTitle = styled.div`
  overflow: hidden;
  grid-area: title;

  font-size: 16px;
  font-weight: 800;
  text-overflow: ellipsis;
  white-space: nowrap;
`;

const Singer = styled.div`
  overflow: hidden;
  grid-area: singer;

  font-size: 12px;
  text-overflow: ellipsis;
  white-space: nowrap;
`;

const Info = styled.div`
  grid-area: info;
  font-size: 12px;
  color: #808191;
`;
