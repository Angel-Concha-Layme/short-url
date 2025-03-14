package dev.shorturl.services;

import dev.shorturl.dto.TagDTO;
import dev.shorturl.exception.TagInUseException;
import dev.shorturl.exception.TagNotFoundException;
import dev.shorturl.model.AppUser;
import dev.shorturl.model.Tag;
import dev.shorturl.repository.TagRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {
  private final TagRepository tagRepository;

  public TagService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @Transactional
  public TagDTO createTag(TagDTO tagDTO, AppUser user) {
    Tag tag = new Tag();
    tag.setName(tagDTO.name());
    tag.setColor(tagDTO.color());
    tag.setAppUser(user);

    Tag savedTag = tagRepository.save(tag);
    return new TagDTO(savedTag.getName(), savedTag.getColor());
  }

  @Transactional(readOnly = true)
  public TagDTO getTagById(Long id) {
    Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(id));
    return TagDTO.of(tag);
  }

  @Transactional(readOnly = true)
  public List<TagDTO> getAllTags(AppUser currentUser) {
    return tagRepository.findByAppUser(currentUser).stream()
        .map(tag -> new TagDTO(tag.getName(), tag.getColor()))
        .collect(Collectors.toList());
  }

  @Transactional
  public TagDTO updateTag(Long id, TagDTO tagDTO, AppUser user) {
    Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(id));
    if (!tag.getAppUser().equals(user)) {
      throw new SecurityException("User not authorized to update this tag");
    }

    tag.setName(tagDTO.name());
    tag.setColor(tagDTO.color());

    Tag updatedTag = tagRepository.save(tag);
    return new TagDTO(updatedTag.getName(), updatedTag.getColor());
  }

  @Transactional
  public void deleteTag(Long id, AppUser user) {
    Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(id));

    if (!tag.getAppUser().equals(user)) {
      throw new SecurityException("User not authorized to delete this tag");
    }

    if (!tag.getLinks().isEmpty()) {
      throw new TagInUseException(id);
    }

    tagRepository.deleteById(id);
  }
}
